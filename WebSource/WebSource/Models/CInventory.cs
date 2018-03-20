using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WebSource.Models
{
    public class CInventory
    {
        public int shop_id { get; set; }
        public CProduct product { get; set; }
        public int? prod_quant { get; set; }
        public CInventory(inventory i, CProduct p)
        {
            shop_id = i.shop_id;
            prod_quant = i.prod_quant;
            product = p;
        }
    }
}