using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using WebSource.Models;

namespace WebSource.Controllers
{
    public class InvoiceController : ApiController
    {

        [ActionName("GetSalesInvoice")]
        public IHttpActionResult getSalesInvoice(String apiKey, string invoiceId)
        {
            List<JInvoice> invoicesList = new List<JInvoice>();

            try
            {
                SMS_DBEntities1 db = new SMS_DBEntities1();
                var user = db.users.FirstOrDefault(y => y.api_key.Equals(apiKey));
                if (null == user)
                {
                    return Ok();
                }
                var shop = db.shops.FirstOrDefault(y => y.shop_id == user.shop_id);

                if (null != db.sales.Where(y => y.sale_id.Contains(invoiceId))) {
                    var salesList = db.sales.Where(y => y.sale_id.Contains(invoiceId)).ToList();
                    foreach (var sales in salesList.GroupBy(x => x.sale_id))
                    {
                        invoicesList.Add(new JInvoice(sales.ToList()));
                    }
                }
                    
                else
                    return Ok(false);
            }
            catch (Exception ex)
            {
                //ok = false;
            }
            finally { }

            return Ok(invoicesList);
        }

        [ActionName("GetAllSaleInvoices")]
        public IHttpActionResult getAllSaleInvoices(String apiKey)
        {
            List<JInvoice> invoices = new List<JInvoice>();
            try
            {
                SMS_DBEntities1 db = new SMS_DBEntities1();
                var user = db.users.FirstOrDefault(y => y.api_key.Equals(apiKey));
                if (null == user)
                {
                    return Ok();
                }
                var shop = db.shops.FirstOrDefault(y => y.shop_id == user.shop_id);
                foreach (var sale in shop.sales.GroupBy(x => x.sale_id))
                {
                    invoices.Add(new JInvoice(sale.ToList()));
                }
                return Ok(invoices);
            }
            catch (Exception ex)
            {
                //ok = false;
            }
            finally { }

            return Ok();
        }

        [ActionName("GetSalesInvoiceByCell")]
        public IHttpActionResult GetSalesInvoiceByCell(String apiKey, String cust_phone)
        {
            JInvoice invoice = null;
            List<JInvoice> invoicesList = new List<JInvoice>();
            try
            {
                SMS_DBEntities1 db = new SMS_DBEntities1();
                var user = db.users.FirstOrDefault(y => y.api_key.Equals(apiKey));
                if (null == user)
                {
                    return Ok();
                }
                var shop = db.shops.FirstOrDefault(y => y.shop_id == user.shop_id);

                if (null != db.sales.Where(y => y.cust_phone.Contains(cust_phone) && y.shop_id == shop.shop_id))
                {
                    var salesList = db.sales.Where(y => y.cust_phone.Contains(cust_phone)).ToList();  // add cust phone for every row of same sale id in db
                    foreach (var salesWithCellNo in salesList.GroupBy(x => x.sale_id))
                    {
                        //eventsInYear.Key - year
                        //eventsInYear - collection of events in that year
                        invoice = new JInvoice(salesWithCellNo.ToList());
                        invoicesList.Add(invoice);
                    }
                    //invoice = new JInvoice(db.sales.Where(y => y.cust_phone == cust_phone).ToList());
                }
                    
                else
                    return Ok(false);
            }
            catch (Exception ex)
            {
                //ok = false;
            }
            finally { }

            return Ok(invoicesList);
        }

        [ActionName("GetPurchaseInvoice")]
        public IHttpActionResult GetPurchaseInvoice(String apiKey, String invoiceId)
        {
            JInvoice invoice = null;
            List<JInvoice> invoicesList = new List<JInvoice>();

            try
            {
                SMS_DBEntities1 db = new SMS_DBEntities1();
                var user = db.users.FirstOrDefault(y => y.api_key.Equals(apiKey));
                if (null == user)
                {
                    return Ok();
                }
                var shop = db.shops.FirstOrDefault(y => y.shop_id == user.shop_id);

                if (null != db.sales.Where(y => y.sale_id.Contains(invoiceId) && y.shop_id == shop.shop_id))
                {
                    var salesList = db.sales.Where(y => y.cust_phone.Contains(invoiceId)).ToList();  // add cust phone for every row of same sale id in db
                    foreach (var saleWithInvoice in salesList.GroupBy(x => x.sale_id))
                    {
                        //eventsInYear.Key - year
                        //eventsInYear - collection of events in that year
                        invoice = new JInvoice(saleWithInvoice.ToList());
                        invoicesList.Add(invoice);
                    }
                    //invoice = new JInvoice(db.sales.Where(y => y.cust_phone == cust_phone).ToList());
                }

                else
                    return Ok(false);

            }
            catch (Exception ex)
            {
                //ok = false;
            }
            finally { }

            return Ok(invoice);
        }

        [HttpGet]
        [ActionName("clearSaleInvoicePayment")]
        public IHttpActionResult clearSaleInvoicePayment(String apiKey, String invoiceId, double amt)
        {
            sale invoice = null;

            try
            {
                SMS_DBEntities1 db = new SMS_DBEntities1();
                var user = db.users.FirstOrDefault(y => y.api_key.Equals(apiKey));
                if (null == user)
                {
                    return Ok();
                }
                var shop = db.shops.FirstOrDefault(y => y.shop_id == user.shop_id);

                invoice = db.sales.FirstOrDefault(y => y.sale_id.Equals(invoiceId));

                if (null == invoice)
                    return Ok(false);

                if(invoice.paid_amt + amt == invoice.total_amt)
                    invoice.is_pmnt_clr = "Y";

                invoice.paid_amt += amt;

                db.SaveChanges();

            }
            catch (Exception ex)
            {
                //ok = false;
            }
            finally { }

            return Ok(true);
        }


    }
}
