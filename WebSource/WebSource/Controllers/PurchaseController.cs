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
                var inventory = shop.inventories.ToList();
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
                    var brand_id = -1;
                    var product_type = -1;

                    if (null == inventory.FirstOrDefault(y => y.product.brand_id == product.brand.brand_id) &&
                        null == inventory.FirstOrDefault(y => y.product.brand.brand_name.Equals(product.brand.brand_name.ToLower())))
                    {
                        db.brands.Add(new brand { brand_name = product.brand.brand_name });
                        db.SaveChanges();
                        brand_id = db.brands.Where(y => y.brand_name.Equals(product.brand.brand_name)).First(y => y.products.Count == 0).brand_id;
                    }
                    else if (null != inventory.FirstOrDefault(y => y.product.brand.brand_name.Equals(product.brand.brand_name.ToLower())))
                    {
                        brand_id = inventory.FirstOrDefault(y => y.product.brand.brand_name.Equals(product.brand.brand_name.ToLower())).product.brand_id;
                    }
                    else if (null != db.brands.FirstOrDefault(y => y.brand_id == product.brand.brand_id) &&
                            product.brand.brand_name != null && !product.brand.brand_name.Equals(""))
                    {
                        brand b = db.brands.FirstOrDefault(y => y.brand_id == product.brand.brand_id);
                        b.brand_name = product.brand.brand_name;
                        db.SaveChanges();
                        brand_id = b.brand_id;
                    }
                    if (null == inventory.FirstOrDefault(y => y.product.product_type == product.product_type.type_id) &&
                        null == inventory.FirstOrDefault(y => y.product.product_types.type_name.ToLower().Equals(product.product_type.type_name.ToLower())))
                    {
                        db.product_types.Add(new product_types { type_name = product.product_type.type_name });
                        db.SaveChanges();
                        product_type = db.product_types.Where(y => y.type_name.Equals(product.product_type.type_name)).First(y => y.products.Count == 0).type_id;
                    }
                    else if (null != inventory.FirstOrDefault(y => y.product.product_types.type_name.ToLower().Equals(product.product_type.type_name.ToLower()))) {
                        product_type = inventory.FirstOrDefault(y => y.product.product_types.type_name.ToLower().Equals(product.product_type.type_name.ToLower())).product.product_type;
                    }
                    else if (null != db.product_types.FirstOrDefault(y => y.type_id == product.product_type.type_id) &&
                            product.product_type.type_name != null && !product.product_type.type_name.Equals(""))
                    {
                        product_types p = db.product_types.FirstOrDefault(y => y.type_id == product.product_type.type_id);
                        p.type_name = product.product_type.type_name;
                        db.SaveChanges();
                        product_type = p.type_id;
                    }
                    int msr_unit = -1;
                    if (null == inventory.FirstOrDefault(y => y.product.msrmnt_units.sr_no == product.unit_of_msrmnt.sr_no) &&
                        null == inventory.FirstOrDefault(y => y.product.msrmnt_units.description.ToLower().Equals(product.unit_of_msrmnt.description)))
                    {
                        db.msrmnt_units.Add(new msrmnt_units { description = product.unit_of_msrmnt.description });
                        db.SaveChanges();
                        msr_unit = db.msrmnt_units.Where(y => y.description.Equals(product.unit_of_msrmnt.description)).First(y => y.products.Count == 0).sr_no;
                    }
                    else if (null != inventory.FirstOrDefault(y => y.product.msrmnt_units.description.ToLower().Equals(product.unit_of_msrmnt.description))){
                        msr_unit = inventory.FirstOrDefault(y => y.product.msrmnt_units.description.ToLower().Equals(product.unit_of_msrmnt.description)).product.unit_of_msrmnt;
                    }
                    else
                    {
                        var msmnt_unit = db.msrmnt_units.FirstOrDefault(y => y.sr_no.Equals(product.unit_of_msrmnt.sr_no));
                        msmnt_unit.description = product.unit_of_msrmnt.description;
                        msr_unit = msmnt_unit.sr_no;
                        db.SaveChanges();
                    }

                    if (null == inventory.FirstOrDefault(y => y.product_id == product.product_id) &&
                        null == inventory.FirstOrDefault(y => y.product.product_name.ToLower().Equals(product.product_name.ToLower())))
                    {                      

                        db.products.Add(new product
                        {
                            product_name = product.product_name,
                            unit_price = product.unit_price,
                            unit_of_msrmnt = msr_unit,
                            specs = product.specs,
                            brand_id = brand_id,
                            product_type = product_type
                        });

                        db.SaveChanges();
                        var pros= db.products.Where(y => y.product_name.ToLower().Equals(product.product_name.ToLower())).First(y=>y.inventories.Count == 0);
                        prod_id = pros.product_id;
                    }
                    

                    if (0 != product.product_id) {
                        prod_id = product.product_id;
                        var prod = db.products.First(y => y.product_id == product.product_id);
                        prod.product_name = product.product_name;
                        db.SaveChanges();
                    }

                    var invObj = inventory.FirstOrDefault(y => y.product_id == prod_id);
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
                            prod_quant = product.qty,
                            is_brand_active = "Y",
                            is_prod_active = "Y"
                        });

                    }

                    foreach (var inv in inventory.Where(y=>y.product.brand_id == brand_id)) {
                        inv.is_brand_active = "Y";
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
