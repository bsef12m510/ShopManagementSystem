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

        [ActionName("GetInvoice")]
        public IHttpActionResult getInvoice(String apiKey, int invoiceId)
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
                if (null == invoice)
                    invoice = new JInvoice(db.purchases.FirstOrDefault(y => y.purch_id == invoiceId));

            }
            catch (Exception ex)
            {
                //ok = false;
            }
            finally { }

            return Ok(invoice);
        }

        [ActionName("GetAllInvoices")]
        public IHttpActionResult getAllInvoices(JInvoice invoice)
        {
            bool ok = true;
            try
            {
                SMS_DBEntities1 db = new SMS_DBEntities1();
                var user = db.users.FirstOrDefault(y => y.api_key.Equals(invoice.apiKey));
                if (null == user)
                {
                    return Ok();
                }
                var shop = db.shops.FirstOrDefault(y => y.shop_id == user.shop_id);

                sale sInvoice = db.sales.FirstOrDefault(y => y.sale_id == invoice.invoiceId && y.is_invoice.equals("Y"));
                if (sInvoice != null)
                {
                    if (invoice.amount_paid == sInvoice.total_amt)
                        sInvoice.is_pmnt_clr = "Y";
                    sInvoice.paid_amt = invoice.amount_paid;
                }
                else
                {
                    purchase pInvoice = db.purchases.FirstOrDefault(y => y.purch_id == invoice.invoiceId && y.is_invoice.equals("Y"));
                    if (sInvoice != null)
                    {
                        if (invoice.amount_paid == pInvoice.total_amt)
                            sInvoice.is_pmnt_clr = "Y";
                        pInvoice.paid_amt = invoice.amount_paid;
                    }
                    else
                        ok = false;
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
