using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using WebSource.Models;

namespace WebSource.Controllers
{
    public class SearchController : ApiController
    {
        [HttpGet]
        [ActionName("SearchByProduct")]
        public IHttpActionResult SearcProduct(String product_name)
        {
            SMS_DBEntities1 db = new SMS_DBEntities1();
            //  IEnumerable<product> products = db.products.Where(x => x.product_name.Equals(product_name));
            var products = new List<product>();
            var user_id = "hrauf";
            var user = db.users.First(y => y.user_id.Equals(user_id));
            var shop = db.shops.First(y => y.shop_id == user.shop_id);
            var inventory = db.inventories.Where(y => y.shop_id == shop.shop_id);
            
            
            //var inventory = db.inventories;



            foreach (var i in inventory)
            {
                var p = db.products.First(y => y.product_id == i.product_id);
                if (db.product_types.First(y=>y.type_id==p.product_type).type_name.Equals("tv"))
                    products.Add(p);
                
            }
            return Ok(products);
        }
    }
}
