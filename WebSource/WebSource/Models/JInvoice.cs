using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WebSource.Models
{
    public class JInvoice
    {
        public int invoiceId{ get; set;}
        public string str_invoiceId { get; set; }
        public int shopID { get; set; }
        public string agentID { get; set; }
        public string cust_name { get; set; }
        public string cust_phone { get; set; }
        public string dlr_details { get; set; }
        public String apiKey { get; set; }
        public List<JProduct> products { get; set; }
        public double amount_paid { get; set; }
        public double total_amount { get; set; }

        public JInvoice(List<sale> invoice)
        {
            this.agentID = invoice.First(y=>y.is_invoice.Equals("Y")).agent_id;
            this.shopID = (int)invoice.First(y=>y.is_invoice.Equals("Y")).shop_id;
            this.invoiceId = (int)invoice.First(y=>y.is_invoice.Equals("Y")).sale_id;
            this.amount_paid = (double)invoice.First(y=>y.is_invoice.Equals("Y")).paid_amt;
            this.total_amount = (double)invoice.First(y=>y.is_invoice.Equals("Y")).total_amt;
            this.cust_name = invoice.First(y => y.is_invoice.Equals("Y")).cust_name;
            this.cust_phone = invoice.First(y => y.is_invoice.Equals("Y")).cust_phone;

            products = new List<JProduct>();
            foreach (var sale in invoice) {
                products.Add(new JProduct { product_id = sale.product.product_id, product_name = sale.product.product_name
                                            , qty = sale.prod_quant, unit_price = sale.product.unit_price });
            }
        }

        public JInvoice(List<purchase> invoice) {
            this.agentID = invoice.First(y=>y.is_invoice.Equals("Y")).agent_id;
            this.shopID = (int)invoice.First(y=>y.is_invoice.Equals("Y")).shop_id;
            this.str_invoiceId = invoice.First(y=>y.is_invoice.Equals("Y")).purch_id;
            this.amount_paid = (double)invoice.First(y=>y.is_invoice.Equals("Y")).paid_amt;
            this.total_amount = (double)invoice.First(y=>y.is_invoice.Equals("Y")).total_amt;
            this.cust_name = invoice.First(y => y.is_invoice.Equals("Y")).dlr_name;
            this.dlr_details = invoice.First(y => y.is_invoice.Equals("Y")).dlr_dtls;
            this.cust_phone = invoice.First(y => y.is_invoice.Equals("Y")).dlr_phone;

            products = new List<JProduct>();
            foreach (var purchase in invoice)
            {
                products.Add(new JProduct { product_id = purchase.product.product_id, product_name = purchase.product.product_name,
                                            qty = purchase.prod_quant, unit_price = purchase.product.unit_price
                });
            }
        }
    }
}