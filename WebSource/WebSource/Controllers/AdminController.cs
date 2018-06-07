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
                if (null != session && null != db.users.FirstOrDefault(y => y.api_key.Equals(session) && y.role_id.Equals("Admin")))
                    return View(db.shops.ToList());

                var username = Request.Form["username"].ToString();
                var pass = Request.Form["password"].ToString();

                if (null != db.users.FirstOrDefault(y => y.user_id.Equals(username)
                    && y.password.Equals(pass) && y.role_id.Equals("Admin")))
                {
                    Session.Add("key", db.users.FirstOrDefault(y => y.user_id.Equals(username)
                    && y.password.Equals(pass)).api_key);
                    var shops = db.shops.ToList();
                    shops.Remove(db.shops.FirstOrDefault(y => y.shope_name.Equals("Admin Shop")));
                    return View(shops);
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
                if (null != session && null != db.users.FirstOrDefault(y => y.api_key.Equals(session) && y.role_id.Equals("Admin")))
                {
                    var users = db.users.Where(y => y.role_id.Equals("Owner") || y.role_id.Equals("Salesman")).ToList();
                    return View(users);
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
                if (null != session && null != db.users.FirstOrDefault(y => y.api_key.Equals(session) && y.role_id.Equals("Admin")))
                {
                    List<user> other = db.users.Where(y => y.role_id.Equals("Owner") || y.role_id.Equals("Salesman")).ToList();
                    var shop = db.shops.FirstOrDefault(y => y.shop_id == shop_id);
                    var mnger = db.users.FirstOrDefault(y => y.user_id.Equals(shop.shop_mngr));

                    if (other.Count > 0)
                    {
                        if (mnger != null)
                        {
                            other.Remove(mnger);
                            ViewBag.shop_mngr = mnger.user_id;
                        }
                        else
                            ViewBag.shop_mngr = "Select A Manager";
                    }
                    
                    ViewBag.others = other;
                    return View(shop);
                }
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
                if (null != session && null != db.users.FirstOrDefault(y => y.api_key.Equals(session) && y.role_id.Equals("Admin")))
                {
                    var shops = db.shops.ToList();
                    var user = db.users.FirstOrDefault(y => y.user_id == user_id);
                    shops.Remove(db.shops.First(y => y.shop_id == user.shop_id));
                    shops.Remove(db.shops.FirstOrDefault(y => y.shope_name.Equals("Admin Shop")));
                    ViewBag.shops = shops;
                    if (user.role_id.Equals("Salesman"))
                        ViewBag.role = "Owner";
                    else
                        ViewBag.role = "Salesman";

                    return View(user);
                }
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
                if (null != session && null != db.users.FirstOrDefault(y => y.api_key.Equals(session) && y.role_id.Equals("Admin")))
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
                if (null != session && null != db.users.FirstOrDefault(y => y.api_key.Equals(session) && y.role_id.Equals("Admin")))
                {
                    var shops = db.shops.ToList();
                    shops.Remove(db.shops.FirstOrDefault(y => y.shope_name.Equals("Admin Shop")));
                    return View(shops);
                } 
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
                if (null != session && null != db.users.FirstOrDefault(y => y.api_key.Equals(session) && y.role_id.Equals("Admin")))
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
                if (null != session && null != db.users.FirstOrDefault(y => y.api_key.Equals(session) && y.role_id.Equals("Admin")))
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
                if (null != session && null != db.users.FirstOrDefault(y => y.api_key.Equals(session) && y.role_id.Equals("Admin")))
                {
                    var user = db.users.FirstOrDefault(y => y.user_id.Equals(user_id));
                    user.username = Request.Form["username"].ToString();
                    user.shop_id = int.Parse(Request.Form["shop"].Split('-').First());
                    user.role_id = Request.Form["role"].ToString();
                    user.password = Request.Form["password"].ToString();

                    if (user.role_id.Equals("Owner")) {
                        var shop = db.shops.First(y => y.shop_id == y.shop_id);
                        shop.shop_mngr = user.user_id;
                    }

                    db.SaveChanges();
                    return RedirectToAction("Users");
                }

            }
            catch (Exception e)
            {
                return RedirectToAction("Users");
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
                if (null != session && null != db.users.FirstOrDefault(y => y.api_key.Equals(session) && y.role_id.Equals("Admin")))
                {
                    var shop = db.shops.FirstOrDefault(y => y.shop_id == shop_id);
                    shop.shope_name = Request.Form["shop_name"].ToString();
                    if (!Request.Form["shop_mng"].ToString().Equals("Select A Manager"))
                    {
                        shop.shop_mngr = Request.Form["shop_mng"].ToString();
                        var user = db.users.First(y => y.user_id.Equals(shop.shop_mngr));
                        user.role_id = "Owner";
                        user.shop_id = shop.shop_id;
                    }

                    shop.phone = Request.Form["phone"].ToString();
                    shop.address = Request.Form["address"].ToString();
                    db.SaveChanges();
                    return RedirectToAction("Shops");
                }

            }
            catch (Exception e)
            {
                return RedirectToAction("Shops");
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
                if (null != session && null != db.users.FirstOrDefault(y => y.api_key.Equals(session) && y.role_id.Equals("Admin")))
                {
                    db.shops.Add(new shop
                    {
                        shope_name = Request.Form["shop_name"].ToString(),
                        phone = Request.Form["phone"].ToString(),
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
                if (null != session && null != db.users.FirstOrDefault(y => y.api_key.Equals(session) && y.role_id.Equals("Admin")))
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

                    if (Request.Form["role"].ToString().Equals("Owner")) {
                        int shop_id = int.Parse(Request.Form["shop"].ToString().Split('-').First().Trim());
                        var shop = db.shops.First(y => y.shop_id == shop_id);
                        shop.shop_mngr = Request.Form["user_id"].ToString();
                    }

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