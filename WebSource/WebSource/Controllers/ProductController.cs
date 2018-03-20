using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using WebSource.Models;

namespace WebSource.Controllers
{
    public class ProductController : ApiController
    {
        [HttpGet]
        [ActionName("GetAllProducts")]
        public IHttpActionResult GetAllProducts(String apiKey)
        {
            SMS_DBEntities1 db = new SMS_DBEntities1();
            var user = db.users.FirstOrDefault(x => x.api_key.Equals(apiKey));
            if (null != user)
            {
                var shop = db.shops.FirstOrDefault(x => x.shop_id == user.shop_id);
                var inventory = db.inventories.Where(x => x.shop_id == shop.shop_id);
                var cinventory = new List<CInventory>();
                foreach (inventory i in inventory)
                {
                    var p = db.products.FirstOrDefault(y => y.product_id == i.product_id);
                    var type = db.product_types.FirstOrDefault(y => y.type_id == p.product_type);
                    var brand = db.brands.FirstOrDefault(y => y.brand_id == p.brand_id);
                    cinventory.Add(new CInventory(i, new CProduct(p, type, brand, 0)));
                }

                return Ok(cinventory);
            }
            else
            {
                return BadRequest();
            }
        }

        [HttpGet]
        [ActionName("GetAllBrandsForProductType")]
        public IHttpActionResult GetAllBrandsForProductType(String apiKey, int productTypeId)
        {
            SMS_DBEntities1 db = new SMS_DBEntities1();
            var user = db.users.FirstOrDefault(x => x.api_key.Equals(apiKey));
            if (null != user)
            {
                var shop = db.shops.FirstOrDefault(x => x.shop_id == user.shop_id);
                var inventory = db.inventories.Where(x => x.shop_id == shop.shop_id);
                var cbrands = new Dictionary<int, CBrand>();
                var cbrands1 = new List<CBrand>();
                IEnumerable<CBrand> distinctList = null;
                foreach (inventory i in inventory)
                {
                    var p = db.products.FirstOrDefault(y => y.product_id == i.product_id && y.product_type == productTypeId);

                    var brand = db.brands.FirstOrDefault(y => y.brand_id == p.brand_id);
                    //  if(!cbrands.ContainsKey(brand.brand_id))
                    //    cbrands[brand.brand_id] = new CBrand(brand);

                    cbrands1.Add(new CBrand(brand));
                    distinctList = cbrands1.GroupBy(x => x.brand_id).Select(x => x.First());
                }

                return Ok(distinctList);
            }
            else
            {
                return BadRequest();
            }
        }
    }
        
}
