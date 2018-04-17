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
            int purch_id = -1;

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
                purch_id = 1;

                List<purchase> purchases = new List<purchase>();

                try {
                    purch_id = db.purchases.Max(p=>p.purch_id) + 1;
                }
                catch (Exception ex) { }

                foreach (var product in purchase.products)
                {
                    var prod_id = product.product_id;
                    
                    if (prod_id == 0)
                    {
                        if (null == db.brands.FirstOrDefault(y => y.brand_id == product.brand.brand_id) &&
                            null == db.brands.FirstOrDefault(y => y.brand_name == product.brand.brand_name))
                        {
                            db.brands.Add(new brand { brand_name = product.brand.brand_name });
                            db.SaveChanges();
                        }
                        if (null == db.product_types.FirstOrDefault(y => y.type_id == product.product_type.type_id) &&
                            null == db.product_types.FirstOrDefault(y => y.type_name == product.product_type.type_name))
                        {
                            db.product_types.Add(new product_types { type_name = product.product_type.type_name });
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
                                unit_of_msrmnt = product.unit_of_msrmnt,
                                specs = product.specs,
                                brand_id = brand_id,
                                product_type = product_type
                            });

                            db.SaveChanges();
                        }
                        prod_id = db.products.First(y => y.product_name.Equals(product.product_name)).product_id;
                    }
                    
                    var invObj = inventory.FirstOrDefault(y => y.product_id == product.product_id);
                    if (invObj != null)
                    {
                        invObj.prod_quant += product.qty;
                    }
                    else {
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
                            purch_id = purch_id,
                            prod_id = prod_id,
                            dlr_name = purchase.dlr_name,
                            prod_quant = (int)product.qty,
                            pur_date = DateTime.Today,
                            pur_time = DateTime.Now.TimeOfDay,
                            shop_id = shop.shop_id,
                            agent_id = user.user_id,
                            is_pmnt_clr = isClr,
                            total_amt = purchase.total_amount,
                            dlr_phone = purchase.dlr_phone,
                            paid_amt = purchase.amount_paid,
                            is_invoice = "Y"

                        });
                    }
                    else {
                        purchases.Add(new purchase
                        {
                            purch_id = purch_id,
                            prod_id = prod_id,
                            dlr_name = purchase.dlr_name,
                            prod_quant = (int)product.qty,
                            pur_date = DateTime.Today,
                            pur_time = DateTime.Now.TimeOfDay,
                            shop_id = shop.shop_id,
                            agent_id = user.user_id,
                            is_pmnt_clr = "N",
                            is_invoice = "N"
                        });
                    }


                    i++;
                }

                foreach (var purchase1 in purchases) {
                    db.purchases.Add(purchase1);
                }

                db.SaveChanges();
            }
            catch (Exception ex)
            {
                purch_id = -1;
            }
            finally { }

            return Ok(purch_id);
        }
    }
}
