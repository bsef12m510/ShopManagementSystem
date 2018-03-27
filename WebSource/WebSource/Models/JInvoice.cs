using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WebSource.Models
{
    public class JInvoice
    {
        public int invoiceId{ get; set;}
        public int shopID { get; set; }
        public string agentID { get; set; }
        public int productID { get; set; }
        public double amount_paid { get; set; }
        public double total_amount { get; set; }

        public JInvoice(sale invoice)
        {
            this.agentID = invoice.agent_id;
            this.shopID = (int)invoice.shop_id;
            this.invoiceId = (int)invoice.sale_id;
            this.productID = (int)invoice.product_id;
            this.amount_paid = (double)invoice.paid_amt;
            this.total_amount = (double)invoice.total_amt;
        }

        public JInvoice(purchase invoice) {
            this.agentID = invoice.agent_id;
            this.shopID = (int)invoice.shop_id;
            this.invoiceId = (int)invoice.purch_id;
            this.productID = (int)invoice.prod_id;
            this.amount_paid = (double)invoice.paid_amt;
            this.total_amount = (double)invoice.total_amt;
        }
    }
}