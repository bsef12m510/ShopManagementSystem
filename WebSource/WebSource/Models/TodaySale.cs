using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WebSource.Models
{
    public class TodaySale
    {
        public int product_id { get; set; }
        public int total_items { get; set; }
        public double total_sale { get; set; }
    }
}