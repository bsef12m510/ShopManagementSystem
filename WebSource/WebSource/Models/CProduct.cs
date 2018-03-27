using System;
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
        public string unit_of_msrmnt { get; set; }
        public double unit_price { get; set; }
        public string product_image { get; set; }
        public int? qty { get; set; }
        public int? otherThanCurrentInventoryQty { get; set; }

        public CProduct(product p, product_types type, brand b, int? qt)
        {
            product_id = p.product_id;
            product_name = p.product_name;
            product_type = new CProductType(type);
            brand = new CBrand(b);
            specs = p.specs;
            unit_of_msrmnt = p.unit_of_msrmnt;
            unit_price = p.unit_price;
            product_image = p.product_image;
            qty = qt;

        }

        public CProduct(product p, product_types type, brand b, int? qt, int? otherQt)
        {
            product_id = p.product_id;
            product_name = p.product_name;
            product_type = new CProductType(type);
            brand = new CBrand(b);
            specs = p.specs;
            unit_of_msrmnt = p.unit_of_msrmnt;
            unit_price = p.unit_price;
            product_image = p.product_image;
            qty = qt;
            otherThanCurrentInventoryQty = otherQt;

        }
    }
}