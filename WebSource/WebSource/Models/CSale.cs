using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WebSource.Models
{
    public class CSale
    {
        public int? shop_id { get; set; }
        public int sale_id { get; set; }
        public CProduct cproduct { get; set; }
        public CUser agent { get; set; }
        public int? prod_qty { get; set; }
        public DateTime saleDate { get; set; }
        public TimeSpan saleTime { get; set; }
        public String cust_name { get; set; }
        public String cust_phone { get; set; }

        public CSale(sale sale, CProduct product, CUser user)
        {
            shop_id = sale.shop_id;
            sale_id = sale.sale_id;
            cproduct = product;
            agent = user;
            prod_qty = sale.prod_quant;
            saleDate = sale.sale_date;
            saleTime = sale.sale_time;
            cust_name = sale.cust_name;
            cust_phone = sale.cust_phone;

        }
    }
}