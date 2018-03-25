using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using WebSource.Models;

namespace WebSource.Controllers
{
    public class SaleController : ApiController
    {
        [HttpPost]
        [ActionName("SaleProduct")]
        public IHttpActionResult saleProducts(String apiKey, IEnumerable<sale> products)
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

                foreach (var product in products)
                {
                    var invObj = inventory.FirstOrDefault(y => y.product_id == product.product_id);
                    if (invObj != null)
                    {
                        if (invObj.prod_quant - product.prod_quant < 0)
                            return Ok(false);
                        invObj.prod_quant -= product.prod_quant;
                        var isClr = "N";
                        if (product.paid_amt == product.total_amt)
                            isClr = "Y";
                        db.sales.Add(new sale
                        {
                            sale_date = DateTime.Today,
                            agent_id = user.user_id,
                            sale_time = DateTime.Now.TimeOfDay,
                            total_amt = product.total_amt,
                            paid_amt = product.paid_amt,
                            discount = product.discount,
                            is_pmnt_clr = isClr,
                            shop_id = shop.shop_id,
                            prod_quant = product.prod_quant,
                            cust_name = product.cust_name,
                            cust_phone = product.cust_phone,
                            product_id = product.product_id
                        });
                    }
                    else
                    {
                        return Ok(false);
                    }
                }
                db.SaveChanges();
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
