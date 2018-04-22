using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WebSource.Models
{
    public class JProduct
    {
        public int product_id { get; set; }
        public string product_name { get; set; }
        public JProductType product_type { get; set; }
        public JBrand brand { get; set; }
        public string specs { get; set; }
        public msrmnt_units unit_of_msrmnt { get; set; }
        public double unit_price { get; set; }
        public string product_image { get; set; }
        public int? qty { get; set; }
    }
}