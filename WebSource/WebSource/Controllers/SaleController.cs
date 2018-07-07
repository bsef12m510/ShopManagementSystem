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
            string sale_id = "";

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

                try {
                    var s_sales = shop.sales.Where(y => y.sale_date.Equals(DateTime.Today.Date));
                    if (shop.sales.Count != 0 && null != s_sales && s_sales.Count() != 0)
                    {
                        int num = s_sales.Max(y => y.sr_no);
                        var sr_no = int.Parse(db.sales.First(y => y.sr_no == num).sale_id.Substring(11))+1;
                        sale_id = DateTime.Today.Date.ToString("ddMMyyyy") + shop.shop_id.ToString("000") + sr_no.ToString("000");
                    }
                    else
                        sale_id = DateTime.Today.Date.ToString("ddMMyyyy")+ shop.shop_id.ToString("000") + "001";
                }
                catch(Exception ex)
                {
                    sale_id = DateTime.Today.Date.ToString("ddMMyyyy") + shop.shop_id.ToString("000") + "001";
                }

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
                                total_amt = product.amount,
                                paid_amt = product.amount,
                                shop_id = shop.shop_id,
                                product_id = product.product_id,
                                prod_quant = (int)product.qty,
                                cust_name = sale.cust_name,
                                cust_phone = sale.cust_phone,
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
                sale_id = "Error";
            }
            finally { }

            return Ok(sale_id);
        }
    }
}
