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
        public IHttpActionResult getSalesInvoice(String apiKey, int invoiceId)
        {
            JInvoice invoice = null;

            try
            {
                SMS_DBEntities1 db = new SMS_DBEntities1();
                var user = db.users.FirstOrDefault(y => y.api_key.Equals(apiKey));
                if (null == user)
                {
                    return Ok();
                }
                var shop = db.shops.FirstOrDefault(y => y.shop_id == user.shop_id);

                invoice = new JInvoice(db.sales.FirstOrDefault(y => y.sale_id == invoiceId));
            }
            catch (Exception ex)
            {
                //ok = false;
            }
            finally { }

            return Ok(invoice);
        }

        [ActionName("GetSalesInvoiceByCell")]
        public IHttpActionResult GetSalesInvoiceByCell(String apiKey, String cust_phone)
        {
            JInvoice invoice = null;

            try
            {
                SMS_DBEntities1 db = new SMS_DBEntities1();
                var user = db.users.FirstOrDefault(y => y.api_key.Equals(apiKey));
                if (null == user)
                {
                    return Ok();
                }
                var shop = db.shops.FirstOrDefault(y => y.shop_id == user.shop_id);

                invoice = new JInvoice(db.sales.FirstOrDefault(y => y.cust_phone == cust_phone));
            }
            catch (Exception ex)
            {
                //ok = false;
            }
            finally { }

            return Ok(invoice);
        }

        [ActionName("GetPurchaseInvoice")]
        public IHttpActionResult GetPurchaseInvoice(String apiKey, String invoiceId)
        {
            JInvoice invoice = null;

            try
            {
                SMS_DBEntities1 db = new SMS_DBEntities1();
                var user = db.users.FirstOrDefault(y => y.api_key.Equals(apiKey));
                if (null == user)
                {
                    return Ok();
                }
                var shop = db.shops.FirstOrDefault(y => y.shop_id == user.shop_id);

                invoice = new JInvoice(db.purchases.FirstOrDefault(y => y.purch_id == str_invoiceId));

                if (null == invoice)
                    return OK(fals);

            }
            catch (Exception ex)
            {
                //ok = false;
            }
            finally { }

            return Ok(invoice);
        }

        [ActionName("clearSaleInvoicePayment")]
        public IHttpActionResult clearSaleInvoicePayment(String apiKey, int invoiceId, double amt)
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

                invoice = db.sale.FirstOrDefault(y => y.sale_id == invoiceId);

                if (null == invoice)
                    return false;

                if(amt == invoice.total_amount)
                    invoice.is_pmnt_clr = "Y";

                invoice.paid_amt = amt;

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
