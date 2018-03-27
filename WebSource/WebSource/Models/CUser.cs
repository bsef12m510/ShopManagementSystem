using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WebSource.Models
{
    public class CUser
    {
        public String user_id { get; set; }
        public String password { get; set; }
        public String role_id { get; set; }
        public String username { get; set; }
        public String api_key { get; set; }
        public int shop_id { get; set; }

        public CUser(user user)
        {
            user_id = user.user_id;
            password = user.password;
            role_id = user.role_id;
            username = user.username;
            api_key = user.api_key;
            shop_id = user.shop_id;
        }
    }
}