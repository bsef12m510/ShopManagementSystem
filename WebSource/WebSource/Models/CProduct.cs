﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WebSource.Models
{
    public class CProduct
    {
        public int product_id { get; set; }
        public string product_name { get; set; }
        public CProductType product_type { get; set; }
        public CBrand brand { get; set; }
        public string specs { get; set; }
        public CUoM unit_of_msrmnt { get; set; }
        public double unit_price { get; set; }
        public string product_image { get; set; }
        public int? qty { get; set; }
        public int? otherThanCurrentInventoryQty { get; set; }
        public CUser user { get; set; }

        public CProduct(product p, product_types type, brand b, msrmnt_units u, int? qt)
        {
            product_id = p.product_id;
            product_name = p.product_name;
            if(null != type)
                product_type = new CProductType(type);
            if(null != b)
                brand = new CBrand(b);
            specs = p.specs;
            if (null != u)
                unit_of_msrmnt = new CUoM(u);
            unit_price = p.unit_price;
            product_image = p.product_image;
            qty = qt;

        }

        public CProduct(product p, product_types type, brand b, msrmnt_units u, int? qt, int? otherQt)
        {
            product_id = p.product_id;
            product_name = p.product_name;
            if (null != type)
                product_type = new CProductType(type);
            if (null != brand)
                brand = new CBrand(b);
            specs = p.specs;
            if(null != u)
                unit_of_msrmnt = new CUoM(u);
            unit_price = p.unit_price;
            product_image = p.product_image;
            qty = qt;
            otherThanCurrentInventoryQty = otherQt;

        }

        public CProduct(product p, product_types type, brand b, msrmnt_units u, int? qt, user usr)
        {
            product_id = p.product_id;
            product_name = p.product_name;
            if (null != type)
                product_type = new CProductType(type);
            if (null != brand)
                brand = new CBrand(b);
            specs = p.specs;
            if (null != u)
                unit_of_msrmnt = new CUoM(u);
            unit_price = p.unit_price;
            product_image = p.product_image;
            qty = qt;
           
            if (null != usr)
                user = new CUser(usr);
        }
    }
}