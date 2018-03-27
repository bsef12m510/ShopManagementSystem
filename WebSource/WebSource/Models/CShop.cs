using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WebSource.Models
{
    public class CShop
    {
        public int shop_id { get; set; }
        public string shop_name { get; set; }
        public string shop_mngr { get; set; }
        public CShop(shop shop)
        {
            shop_id = shop.shop_id;
            shop_name = shop.shope_name;
            shop_mngr = shop.shop_mngr;
        }
    }
}