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
        public IHttpActionResult saleProducts(JSale sale)
        {
            int sale_id = -1;

            try
            {
                SMS_DBEntities1 db = new SMS_DBEntities1();
                var user = db.users.FirstOrDefault(y => y.api_key.Equals(sale.apiKey));
                if (null == user)
                {
                    return Ok(sale_id);
                }
                var shop = db.shops.FirstOrDefault(y => y.shop_id == user.shop_id);
                var inventory = db.inventories.Where(y => y.shop_id == shop.shop_id);
                int i = 1;
                sale_id = 1;
                try {
                    sale_id = db.sales.Max(y => y.sale_id)+1;
                }
                catch(Exception ex)
                {}

                foreach (var product in sale.products)
                {
                    var invObj = inventory.FirstOrDefault(y => y.product_id == product.product_id);
                    if (invObj != null)
                    {
                        if (invObj.prod_quant - product.qty < 0)
                            return Ok(-1);
                        invObj.prod_quant -= product.qty;
                        var isClr = "N";
                        if(i == sale.products.Length) { 
                            if (sale.amount_paid == sale.total_amount)
                                isClr = "Y";
                            db.sales.Add(new sale
                            {
                                sale_id = sale_id,
                                sale_date = DateTime.Today,
                                agent_id = user.user_id,
                                sale_time = DateTime.Now.TimeOfDay,
                                total_amt = sale.total_amount,
                                paid_amt = sale.amount_paid,
                                discount = sale.discount,
                                is_pmnt_clr = isClr,
                                shop_id = shop.shop_id,
                                prod_quant = (int)product.qty,
                                cust_name = sale.cust_name,
                                cust_phone = sale.cust_phone,
                                product_id = product.product_id,
                                is_invoice = "Y"
                            });
                        }
                        else
                        {
                            db.sales.Add(new sale
                            {
                                sale_id = sale_id,
                                sale_date = DateTime.Today,
                                agent_id = user.user_id,
                                sale_time = DateTime.Now.TimeOfDay,
                                shop_id = shop.shop_id,
                                product_id = product.product_id,
                                prod_quant = (int)product.qty,
                                is_invoice = "N",
                                is_pmnt_clr = "N"
                            });
                        }
                    }
                    else
                    {
                        return Ok(-1);
                    }

                    i++;
                }
                db.SaveChanges();
            }
            catch (Exception ex)
            {
                sale_id = -1;
            }
            finally { }

            return Ok(sale_id);
        }
    }
}
