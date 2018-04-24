using System;
using System.Collections.Generic;
using System.Linq;
using System.Web.Http;
using WebSource.Models;

namespace WebSource.Controllers
{
    public class PurchaseController : ApiController
    {
        [HttpPost]
        [ActionName("PurchaseProduct")]
        public IHttpActionResult purchaseProducts(JPurchase purchase)
        {

            try
            {
                SMS_DBEntities1 db = new SMS_DBEntities1();
                var user = db.users.FirstOrDefault(y => y.api_key.Equals(purchase.apiKey));
                if (null == user)
                {
                    return Ok(-1);
                }
                var shop = db.shops.FirstOrDefault(y => y.shop_id == user.shop_id);
                var inventory = db.inventories.Where(y => y.shop_id == shop.shop_id);
                int i = 1;

                List<purchase> purchases = new List<purchase>();

                try
                {
                    if (null != db.purchases.FirstOrDefault(y => y.purch_id == purchase.purch_id))
                    {
                        return Ok(-1);
                    }
                }
                catch (Exception ex) { }

                foreach (var product in purchase.products)
                {
                    var prod_id = product.product_id;


                    if (null == db.brands.FirstOrDefault(y => y.brand_id == product.brand.brand_id) &&
                        null == db.brands.FirstOrDefault(y => y.brand_name == product.brand.brand_name))
                    {
                        db.brands.Add(new brand { brand_name = product.brand.brand_name });
                        db.SaveChanges();
                    }
                    else if (null != db.brands.FirstOrDefault(y => y.brand_id == product.brand.brand_id) &&
                            product.brand.brand_name != null && !product.brand.brand_name.Equals(""))
                    {
                        brand b = db.brands.FirstOrDefault(y => y.brand_id == product.brand.brand_id);
                        b.brand_name = product.brand.brand_name;
                        db.SaveChanges();
                    }
                    if (null == db.product_types.FirstOrDefault(y => y.type_id == product.product_type.type_id) &&
                        null == db.product_types.FirstOrDefault(y => y.type_name == product.product_type.type_name))
                    {
                        db.product_types.Add(new product_types { type_name = product.product_type.type_name });
                        db.SaveChanges();
                    }
                    else if (null != db.product_types.FirstOrDefault(y => y.type_id == product.product_type.type_id) &&
                            product.product_type.type_name != null && !product.product_type.type_name.Equals(""))
                    {
                        product_types p = db.product_types.FirstOrDefault(y => y.type_id == product.product_type.type_id);
                        p.type_name = product.product_type.type_name;
                        db.SaveChanges();
                    }
                    if (null == db.msrmnt_units.FirstOrDefault(y => y.sr_no.Equals(product.unit_of_msrmnt.sr_no)))
                    {
                        db.msrmnt_units.Add(new msrmnt_units { description = product.unit_of_msrmnt.description });
                        db.SaveChanges();
                    }
                    else
                    {
                        var msmnt_unit = db.msrmnt_units.FirstOrDefault(y => y.sr_no.Equals(product.unit_of_msrmnt.sr_no));
                        msmnt_unit.description = product.unit_of_msrmnt.description;
                        db.SaveChanges();
                    }
                    if (null == db.products.FirstOrDefault(y => y.product_id == product.product_id))
                    {
                        var brand_id = product.brand.brand_id;
                        var product_type = product.product_type.type_id;

                        if (product_type == 0)
                            product_type = db.product_types.FirstOrDefault(y => y.type_name == product.product_type.type_name).type_id;
                        if (brand_id == 0)
                            brand_id = db.brands.FirstOrDefault(y => y.brand_name == product.brand.brand_name).brand_id;

                        db.products.Add(new product
                        {
                            product_name = product.product_name,
                            unit_price = product.unit_price,
                            unit_of_msrmnt = product.unit_of_msrmnt.description,
                            specs = product.specs,
                            brand_id = brand_id,
                            product_type = product_type
                        });

                        db.SaveChanges();
                    }
                    

                    if (0 != product.product_id) {
                        prod_id = product.product_id;
                        var prod = db.products.First(y => y.product_name.Equals(product.product_id));
                        prod.product_name = product.product_name;
                        db.SaveChanges();
                    }
                    else
                        prod_id = db.products.First(y => y.product_name.Equals(product.product_name)).product_id;

                    var invObj = inventory.FirstOrDefault(y => y.product_id == product.product_id);
                    if (invObj != null)
                    {
                        invObj.prod_quant += product.qty;
                    }
                    else
                    {
                        db.inventories.Add(new inventory
                        {
                            product_id = prod_id,
                            shop_id = shop.shop_id,
                            prod_quant = product.qty
                        });
                    }

                    if (i == purchase.products.Length)
                    {
                        var isClr = "N";
                        if (purchase.amount_paid == purchase.total_amount)
                            isClr = "Y";

                        purchases.Add(new purchase
                        {
                            purch_id = purchase.purch_id,
                            prod_id = prod_id,
                            dlr_dtls = purchase.dlr_info,
                            prod_quant = (int)product.qty,
                            pur_date = purchase.purch_dtime.Date,
                            pur_time = purchase.purch_dtime.TimeOfDay,
                            shop_id = shop.shop_id,
                            agent_id = user.user_id,
                            is_pmnt_clr = isClr,
                            total_amt = purchase.total_amount,
                            paid_amt = purchase.amount_paid,
                            is_invoice = "Y"

                        });
                    }
                    else
                    {
                        purchases.Add(new purchase
                        {
                            purch_id = purchase.purch_id,
                            prod_id = prod_id,
                            prod_quant = (int)product.qty,
                            pur_date = purchase.purch_dtime.Date,
                            pur_time = purchase.purch_dtime.TimeOfDay,
                            shop_id = shop.shop_id,
                            agent_id = user.user_id,
                            is_pmnt_clr = "N",
                            is_invoice = "N"
                        });
                    }


                    i++;
                }

                foreach (var purchase1 in purchases)
                {
                    db.purchases.Add(purchase1);
                }

                db.SaveChanges();
            }
            catch (Exception ex)
            {
                return Ok(-1);
            }
            finally { }

            return Ok(purchase.purch_id);
        }
    }
}
