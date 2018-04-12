using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WebSource.Models
{
    public class JPurchase
    {
        public JProduct[] products { get; set; }
        public String dlr_name { get; set; }
        public String dlr_phone { get; set; }
        public double amount_paid { get; set; }
        public double total_amount { get; set; }
        public double discount { get; set; }
    }
}