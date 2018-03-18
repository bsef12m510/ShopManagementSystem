using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using WebSource.Models;

namespace WebSource.Controllers
{
    public class ShopController : ApiController
    {
        [HttpGet]
        [ActionName("GetShop")]
        public IHttpActionResult GetShop(String apiKey , String shopName)
        {
            SMS_DBEntities1 db = new SMS_DBEntities1();
            var user = db.users.FirstOrDefault(x => x.api_key.Equals(apiKey));
            if (user.role_id.Equals("admin"))
            {
                var shop = db.shops.FirstOrDefault(x => x.shope_name.Equals(shopName));
              
                return Ok(new CShop(shop));
            }
            else
            {
                return BadRequest();
            }
            
        }


        [HttpGet]
        [ActionName("GetAllShops")]
        public IHttpActionResult GetAllShops(String apiKey)
        {
            SMS_DBEntities1 db = new SMS_DBEntities1();
            var user = db.users.FirstOrDefault(x => x.api_key.Equals(apiKey));
            if (user.role_id.Equals("admin"))
            {
                var shops = db.shops.ToList();
                var cshops = new List<CShop>();
                foreach (shop s in shops)
                    cshops.Add(new CShop(s));
                return Ok(cshops);
            }
            else
            {
                return BadRequest();
            }

        }

        [HttpPost]
        [ActionName("CreateShop")]
        public IHttpActionResult Post(String apiKey, [FromBody]shop shop)
        {
            SMS_DBEntities1 db = new SMS_DBEntities1();
            var user = db.users.FirstOrDefault(x => x.api_key.Equals(apiKey));
            if (user.role_id.Equals("admin"))
            {
                db.shops.Add(shop);
                db.SaveChanges();
                return Ok(new CShop(shop));
            }

            return BadRequest();
        }

        [HttpPut]
        [ActionName("UpdateShop")]
        public IHttpActionResult Put(String apiKey, int id, [FromBody]shop updatedShop)
        {
            SMS_DBEntities1 db = new SMS_DBEntities1();
            var user = db.users.FirstOrDefault(x => x.api_key.Equals(apiKey));
            if (user.role_id.Equals("admin"))
            {
                var shop = db.shops.FirstOrDefault(x => x.shop_id == id);
                if (shop != null)
                {
                    shop.shope_name = updatedShop.shope_name;
                    shop.shop_mngr = updatedShop.shop_mngr;
                    db.Entry(shop).State = System.Data.Entity.EntityState.Modified;
                    db.SaveChanges();
                    return Ok(new CShop(shop));
                }
            }
            return BadRequest();
        }

        [HttpDelete]
        [ActionName("DeleteShop")]
        public IHttpActionResult Delete(String apiKey, int id)
        {
            SMS_DBEntities1 db = new SMS_DBEntities1();
            var user = db.users.FirstOrDefault(x => x.api_key.Equals(apiKey));
            if (user.role_id.Equals("admin"))
            {
                var shops = db.shops.ToList();

                List<string> usernames = new List<string>();
                foreach (var shop in shops)
                {
                    if (shop.shop_id == id)
                    {
                        shops.Remove(shop);
                        db.Entry(shop).State = System.Data.Entity.EntityState.Deleted;
                        db.SaveChanges();
                        return Ok(new CShop(shop));
                    }

                }
                return BadRequest();
            }
            else
            {
                return BadRequest();
            }
        }
    }
}

   


