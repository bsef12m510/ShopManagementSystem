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
          
            foreach(var p in db.products.ToList())
            {
                if (p.product_name.Equals(product_name))
                    products.Add(p);
            } 
            return Ok(products);
        }

    }
}
