using DAL.TimeUuidGenerator;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using WebSource.Models;

namespace WebSource.Controllers
{
    //[Authorize]
    public class ValuesController : ApiController
    {
        // GET api/values
        public IEnumerable<string> GetUsers()
        {
            SMS_DBEntities1 db = new SMS_DBEntities1();
            var users = db.users.ToList();
            List<string> usernames = new List<string>();
            foreach (var user in users) {
                usernames.Add(user.user_id);
            }
            return usernames.ToArray();
            //return new string[] { "ali", "hassan" };
        }

        // GET api/values/5
        public IHttpActionResult Get(string id)
        {
            SMS_DBEntities1 db = new SMS_DBEntities1();
            var users = db.users.ToList();
       
            List<string> usernames = new List<string>();
            foreach (var user in users)
            {
                if(user.user_id.Equals(id))
                {
                    return Ok(user);
                }

            }
            return BadRequest();
        }

        [HttpGet]
        [ActionName("login")]
        public IHttpActionResult Login(String userId, String pswd)
        {
            SMS_DBEntities1 db = new SMS_DBEntities1();
            var user = db.users.FirstOrDefault(x => x.user_id.Equals(userId) && x.password.Equals(pswd));
            if (user != null)
                return Ok(new CUser(user));
            else
                return Ok(false);
        }

        // POST api/values
        public void Post([FromBody]user user)
        {
            SMS_DBEntities1 db = new SMS_DBEntities1();
           
            user.api_key = GuidGenerator.GenerateTimeBasedGuid().ToString();
            db.users.Add(user);
            db.SaveChanges();

        }

        // PUT api/values/5
        public void Put(int id, [FromBody]string value)
        {
        }

        // DELETE api/values/5
        public IHttpActionResult Delete(string id)
        {
            SMS_DBEntities1 db = new SMS_DBEntities1();
            var users = db.users.ToList();

            List<string> usernames = new List<string>();
            foreach (var user in users)
            {
                if (user.user_id.Equals(id))
                {
                    users.Remove(user);
                    db.Entry(user).State = System.Data.Entity.EntityState.Deleted;
                    db.SaveChanges();
                    return Ok(user);
                }

            }
            return BadRequest();
        }

        [HttpGet]
        [ActionName("deleteBrand")]
        public IHttpActionResult deleteBrand(string apiKey, int brand)
        {
            try
            {
                SMS_DBEntities1 db = new SMS_DBEntities1();
                var user = db.users.FirstOrDefault(y => y.api_key.Equals(apiKey));
                var shop = db.shops.FirstOrDefault(y => y.shop_id == user.shop_id);

                db.products.RemoveRange(db.products.Where(y => y.brand_id == brand));
                db.inventories.RemoveRange(shop.inventories.Where(y => y.product.brand_id == brand));
                
                db.SaveChanges();
            }
            catch (Exception ex) {
                return Ok(-1);
            }
        
            return Ok(true);
        }

        [HttpGet]
        [ActionName("deleteProductType")]
        public IHttpActionResult deleteProductType(string apiKey, int p)
        {
            try
            {
                SMS_DBEntities1 db = new SMS_DBEntities1();
                var user = db.users.FirstOrDefault(y => y.api_key.Equals(apiKey));
                var shop = db.shops.FirstOrDefault(y => y.shop_id == user.shop_id);


                db.products.RemoveRange(db.products.Where(y=>y.product_type == p));
                db.inventories.RemoveRange(shop.inventories.Where(y => y.product.product_type == p));
                db.product_types.RemoveRange(db.product_types.Where(y => y.type_id == p));
                db.SaveChanges();
            }
            catch (Exception ex)
            {
                return Ok(-1);
            }

            return Ok(true);
        }

        [HttpGet]
        [ActionName("deleteProduct")]
        public IHttpActionResult deleteProduct(string apiKey, int p)
        {
            try
            {
                SMS_DBEntities1 db = new SMS_DBEntities1();
                var user = db.users.FirstOrDefault(y => y.api_key.Equals(apiKey));
                var shop = db.shops.FirstOrDefault(y => y.shop_id == user.shop_id);

                db.inventories.RemoveRange(shop.inventories.Where(y => y.product_id == p));
                db.products.Remove(db.products.First(y => y.product_id == p));
                db.SaveChanges();
            }
            catch (Exception ex)
            {
                return Ok(-1);
            }

            return Ok(true);
        }
    }
}
