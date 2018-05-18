using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using WebSource.Models;

namespace WebSource.Controllers
{
    public class AdminController : Controller
    {
        // GET: Admin
        //[HttpPost]
        public ActionResult Shops()
        {
            SMS_DBEntities1 db = new SMS_DBEntities1();

            String session = "";

            try
            {
                if (null != Session && null != Session["key"])
                    session = Session["key"].ToString();
                if (null != session && null != db.users.FirstOrDefault(y => y.api_key.Equals(session)))
                    return View(db.shops.ToList());

                var username = Request.Form["username"].ToString();
                var pass = Request.Form["password"].ToString();

                if (null != db.users.FirstOrDefault(y => y.username.Equals(username)
                    && y.password.Equals(pass)))
                {
                    Session.Add("key", db.users.FirstOrDefault(y => y.username.Equals(username)
                    && y.password.Equals(pass)).api_key);
                    return View(db.shops.ToList());
                }
            }
            catch (Exception e) { }
            return RedirectToAction("Login");
        }

        public ActionResult Users()
        {
            SMS_DBEntities1 db = new SMS_DBEntities1();

            String session = "";

            try
            {
                if (null != Session && null != Session["key"])
                    session = Session["key"].ToString();
                if (null != session && null != db.users.FirstOrDefault(y => y.api_key.Equals(session)))
                    return View(db.users.ToList());

                var username = Request.Form["username"].ToString();
                var pass = Request.Form["password"].ToString();

                if (null != db.users.FirstOrDefault(y => y.username.Equals(username)
                    && y.password.Equals(pass)))
                {
                    Session.Add("key", db.users.FirstOrDefault(y => y.username.Equals(username)
                    && y.password.Equals(pass)).api_key);
                    return View(db.users.ToList());
                }
            }
            catch (Exception e) { }

            return RedirectToAction("Login");
        }

        [HttpGet]
        public ActionResult EditShop(int shop_id)
        {
            SMS_DBEntities1 db = new SMS_DBEntities1();

            String session = "";

            try
            {
                if (null != Session && null != Session["key"])
                    session = Session["key"].ToString();
                if (null != session && null != db.users.FirstOrDefault(y => y.api_key.Equals(session)))
                    return View(db.shops.FirstOrDefault(y => y.shop_id == shop_id));
            }
            catch (Exception e) { }
            return RedirectToAction("Login");
        }

        [HttpGet]
        public ActionResult EditUser(String user_id)
        {
            SMS_DBEntities1 db = new SMS_DBEntities1();

            String session = "";

            try
            {
                if (null != Session && null != Session["key"])
                    session = Session["key"].ToString();
                if (null != session && null != db.users.FirstOrDefault(y => y.api_key.Equals(session)))
                    return View(db.users.FirstOrDefault(y => y.user_id == user_id));
            }
            catch (Exception e) { }
            return RedirectToAction("Login");
        }

        public ActionResult AddShop()
        {
            SMS_DBEntities1 db = new SMS_DBEntities1();

            String session = "";

            try
            {
                if (null != Session && null != Session["key"])
                    session = Session["key"].ToString();
                if (null != session && null != db.users.FirstOrDefault(y => y.api_key.Equals(session)))
                    return View();
            }
            catch (Exception e) { }
            return RedirectToAction("Login");
        }

        public ActionResult AddUser()
        {
            SMS_DBEntities1 db = new SMS_DBEntities1();

            String session = "";

            try
            {
                if (null != Session && null != Session["key"])
                    session = Session["key"].ToString();
                if (null != session && null != db.users.FirstOrDefault(y => y.api_key.Equals(session)))
                    return View(db.shops.ToList());
            }
            catch (Exception e) { }
            return RedirectToAction("Login");
        }

        [HttpGet]
        public ActionResult DeleteUser(String user_id)
        {
            SMS_DBEntities1 db = new SMS_DBEntities1();

            try
            {
                String session = "";
                if (null != Session && null != Session["key"])
                    session = Session["key"].ToString();
                if (null != session && null != db.users.FirstOrDefault(y => y.api_key.Equals(session)))
                {
                    db.users.Remove(db.users.FirstOrDefault(y => y.user_id.Equals(user_id)));
                    db.SaveChanges();
                    return RedirectToAction("Users");
                }

            }
            catch (Exception e)
            {
            }
            return RedirectToAction("Login");
        }

        [HttpGet]
        public ActionResult DeleteShop(int shop_id)
        {
            SMS_DBEntities1 db = new SMS_DBEntities1();

            try
            {
                String session = "";
                if (null != Session && null != Session["key"])
                    session = Session["key"].ToString();
                if (null != session && null != db.users.FirstOrDefault(y => y.api_key.Equals(session)))
                {
                    db.inventories.RemoveRange(db.inventories.Where(y => y.shop_id == shop_id));
                    db.purchases.RemoveRange(db.purchases.Where(y => y.shop_id == shop_id));
                    db.sales.RemoveRange(db.sales.Where(y => y.shop_id == shop_id));
                    db.users.RemoveRange(db.users.Where(y => y.shop_id == shop_id));
                    db.SaveChanges();
                    db.shops.Remove(db.shops.FirstOrDefault(y => y.shop_id == shop_id));
                    db.SaveChanges();
                    return RedirectToAction("Shops");
                }

            }
            catch (Exception e)
            {

            }
            return RedirectToAction("Login");
        }

        [HttpPost]
        public ActionResult SaveUserEdit()
        {
            SMS_DBEntities1 db = new SMS_DBEntities1();
            try
            {
                var user_id = Request.Form["user_id"].ToString();
                String session = "";
                if (null != Session && null != Session["key"])
                    session = Session["key"].ToString();
                if (null != session && null != db.users.FirstOrDefault(y => y.api_key.Equals(session)))
                {
                    var user = db.users.FirstOrDefault(y => y.user_id.Equals(user_id));
                    user.username = Request.Form["username"].ToString();
                    user.shop_id = int.Parse(Request.Form["shop"]);
                    user.password = Request.Form["password"].ToString();
                    user.role_id = Request.Form["role"].ToString();
                    db.SaveChanges();
                    return RedirectToAction("Users");
                }

            }
            catch (Exception e)
            {

            }
            return RedirectToAction("Login");
        }
        [HttpPost]
        public ActionResult SaveShopEdit()
        {
            SMS_DBEntities1 db = new SMS_DBEntities1();
            try
            {
                var shop_id = int.Parse(Request.Form["shop_id"].ToString());
                String session = "";
                if (null != Session && null != Session["key"])
                    session = Session["key"].ToString();
                if (null != session && null != db.users.FirstOrDefault(y => y.api_key.Equals(session)))
                {
                    var shop = db.shops.FirstOrDefault(y => y.shop_id == shop_id);
                    shop.shope_name = Request.Form["shop_name"].ToString();
                    shop.shop_mngr = Request.Form["manager"].ToString();
                    shop.address = Request.Form["address"].ToString();
                    db.SaveChanges();
                    return RedirectToAction("Shops");
                }

            }
            catch (Exception e)
            {

            }
            return RedirectToAction("Login");
        }

        [HttpPost]
        public ActionResult AddNewShop()
        {
            SMS_DBEntities1 db = new SMS_DBEntities1();
            try
            {
                String session = "";
                if (null != Session && null != Session["key"])
                    session = Session["key"].ToString();
                if (null != session && null != db.users.FirstOrDefault(y => y.api_key.Equals(session)))
                {
                    db.shops.Add(new shop
                    {
                        shope_name = Request.Form["shop_name"].ToString(),
                        shop_mngr = Request.Form["manager"].ToString(),
                        address = Request.Form["address"].ToString()
                    });

                    db.SaveChanges();
                    return RedirectToAction("Shops");
                }

            }
            catch (Exception e)
            {

            }
            return RedirectToAction("Login");
        }
        [HttpPost]
        public ActionResult AddNewUser()
        {
            SMS_DBEntities1 db = new SMS_DBEntities1();
            try
            {
                String session = "";
                if (null != Session && null != Session["key"])
                    session = Session["key"].ToString();
                if (null != session && null != db.users.FirstOrDefault(y => y.api_key.Equals(session)))
                {
                    db.users.Add(new user
                    {
                        user_id = Request.Form["user_id"].ToString(),
                        username = Request.Form["username"].ToString(),
                        shop_id = int.Parse(Request.Form["shop"].ToString().Split('-').First().Trim()),
                        role_id = Request.Form["role"].ToString(),
                        api_key = Request.Form["user_id"].ToString(),
                        password = Request.Form["password"].ToString()
                    });

                    db.SaveChanges();
                    return RedirectToAction("Users");
                }

            }
            catch (Exception e)
            {

            }
            return RedirectToAction("Login");
        }
        public ActionResult Login()
        {
            if (null != Session["key"])
                return RedirectToAction("Shops");
            return View();
        }

        public ActionResult Logout()
        {
            Session.Clear();
            return RedirectToAction("Login");
        }

    }
}