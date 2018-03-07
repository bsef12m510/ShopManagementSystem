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
        public string Get(int id)
        {
            return "value";
        }

        // POST api/values
        public void Post([FromBody]string value)
        {
        }

        // PUT api/values/5
        public void Put(int id, [FromBody]string value)
        {
        }

        // DELETE api/values/5
        public void Delete(int id)
        {
        }
    }
}
