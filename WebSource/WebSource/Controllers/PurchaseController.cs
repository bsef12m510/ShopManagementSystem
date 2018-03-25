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
        public IHttpActionResult purchaseProducts(String apiKey, CPurchase[] purchases)
        {
            bool ok = true;

            try
            {
                SMS_DBEntities1 db = new SMS_DBEntities1();
                var user = db.users.FirstOrDefault(y => y.api_key.Equals(apiKey));
                if (null == user)
                {
                    return Ok(false);
                }
                var shop = db.shops.FirstOrDefault(y => y.shop_id == user.shop_id);
                var inventory = db.inventories.Where(y => y.shop_id == shop.shop_id);


                foreach (var purchase in purchases)
                {
                    var prod_id = purchase.product.product_id;
                    
                    if (prod_id == 0)
                    {
                        if (null == db.brands.FirstOrDefault(y => y.brand_id == purchase.product.brand.brand_id) &&
                            null == db.brands.FirstOrDefault(y => y.brand_name == purchase.product.brand.brand_name))
                        {
                            db.brands.Add(new brand { brand_name = purchase.product.brand.brand_name });
                            db.SaveChanges();
                        }
                        if (null == db.product_types.FirstOrDefault(y => y.type_id == purchase.product.product_type.type_id) &&
                            null == db.product_types.FirstOrDefault(y => y.type_name == purchase.product.product_type.type_name))
                        {
                            db.product_types.Add(new product_types { type_name = purchase.product.product_type.type_name });
                            db.SaveChanges();
                        }
                        if (null == db.products.FirstOrDefault(y => y.product_id == purchase.product.product_id))
                        {
                            var brand_id = purchase.product.brand.brand_id;
                            var product_type = purchase.product.product_type.type_id;

                            if (product_type == 0)
                                product_type = db.product_types.FirstOrDefault(y => y.type_name == purchase.product.product_type.type_name).type_id;
                            if (brand_id == 0)
                                brand_id = db.brands.FirstOrDefault(y => y.brand_name == purchase.product.brand.brand_name).brand_id;

                            db.products.Add(new product
                            {
                                product_name = purchase.product.product_name,
                                unit_price = purchase.product.unit_price,
                                unit_of_msrmnt = purchase.product.unit_of_msrmnt,
                                specs = purchase.product.specs,
                                brand_id = brand_id,
                                product_type = product_type
                            });

                            db.SaveChanges();
                        }
                        prod_id = db.products.First(y => y.product_name.Equals(purchase.product.product_name)).product_id;
                    }
                    
                    var invObj = inventory.FirstOrDefault(y => y.product_id == purchase.product.product_id);
                    if (invObj != null)
                    {
                        invObj.prod_quant += purchase.product.qty;
                    }
                    else {
                        db.inventories.Add(new inventory
                        {
                            product_id = prod_id,
                            shop_id = shop.shop_id,
                            prod_quant = purchase.product.qty
                        });
                    }
                    
                    var isClr = "N";
                    if (purchase.paid_amt == purchase.total_amt)
                        isClr = "Y";

                    db.purchases.Add(new purchase
                    {
                        prod_id = prod_id,
                        dlr_name = purchase.dlr_name,
                        prod_quant = (int)purchase.product.qty,
                        pur_date = DateTime.Today,
                        pur_time = DateTime.Now.TimeOfDay,
                        shop_id = shop.shop_id,
                        agent_id = user.user_id,
                        is_pmnt_clr = isClr,
                        total_amt = purchase.total_amt,
                        dlr_phone = purchase.dlr_phone,
                        paid_amt = purchase.paid_amt
                    });

                    db.SaveChanges();

                }

            }
            catch (Exception ex)
            {
                ok = false;
            }
            finally { }

            return Ok(ok);
        }
    }
}
