using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WebSource.Models
{
    public class CBrand
    {
        public int brand_id { get; set; }
        public string brand_name { get; set; }
        public string brand_icon { get; set; }
        public CBrand(brand brand)
        {
            brand_id = brand.brand_id;
            brand_name = brand.brand_name;
            brand_icon = brand.brand_icon;
        }


    }
}