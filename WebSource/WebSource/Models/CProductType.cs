using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WebSource.Models
{
    public class CProductType
    {
        public int type_id { get; set; }
        public string type_name { get; set; }

        public CProductType(product_types type)
        {
            type_id = type.type_id;
            type_name = type.type_name;
        }
    }
}