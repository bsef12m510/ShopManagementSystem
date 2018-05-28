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
        public IHttpActionResult SearcProduct(String userId, String product)
        {
            SMS_DBEntities1 db = new SMS_DBEntities1();
            //  IEnumerable<product> products = db.products.Where(x => x.product_name.Equals(product_name));
            var products = new List<product>();
            var user_id = userId;
            var user = db.users.FirstOrDefault(y => y.api_key.Equals(user_id));
            var shop = db.shops.FirstOrDefault(y => y.shop_id == user.shop_id);
            var inventory = db.inventories.Where(y => y.shop_id == shop.shop_id && !(y.is_brand_active.Equals("N") || y.is_prod_active.Equals("N")));


            //var inventory = db.inventories;



            //foreach (var i in inventory)
            //{
            //    var p = db.products.first(y => y.product_id == i.product_id);
            //    if (db.product_types.first(y=>y.type_id==p.product_type).type_name.equals(product))
            //        products.add(p);

            //}
            // return Ok(products);

            var cproducts = new List<CInventory>();
            foreach (var i in inventory)
            {
                var p = db.products.FirstOrDefault(y => y.product_id == i.product_id);
                if (null != p)
                {
                    var type = db.product_types.FirstOrDefault(y => y.type_id == p.product_type);
                    var brand = db.brands.FirstOrDefault(y => y.brand_id == p.brand_id);
                    var uom = db.msrmnt_units.FirstOrDefault(y => y.sr_no == p.unit_of_msrmnt);
                    if (type.type_name.IndexOf(product, 0, StringComparison.CurrentCultureIgnoreCase) != -1)
                    {
                        // The string exists in the original
                        cproducts.Add(new CInventory(i, new CProduct(p, type, brand, uom, 0)));
                    }
                }

            }
            return Ok(cproducts);
        }

        [HttpGet]
        [ActionName("SearchByBrand")]
        public IHttpActionResult SearchBrand(string apiKey, string brandName)
        {
            SMS_DBEntities1 db = new SMS_DBEntities1();
            var user = db.users.FirstOrDefault(y => y.api_key.Equals(apiKey));
            var shop = db.shops.FirstOrDefault(y => y.shop_id == user.shop_id);
            var inventory = db.inventories.Where(y => y.shop_id == shop.shop_id && !(y.is_brand_active.Equals("N") || y.is_prod_active.Equals("N")));

            var cproducts = new List<CInventory>();
            foreach (var i in inventory)
            {
                var p = db.products.FirstOrDefault(y => y.product_id == i.product_id);
                if (null != p)
                {
                    var type = db.product_types.FirstOrDefault(y => y.type_id == p.product_type);
                    var brand = db.brands.FirstOrDefault(y => y.brand_id == p.brand_id);
                    var uom = db.msrmnt_units.FirstOrDefault(y => y.sr_no == p.unit_of_msrmnt);
                    if (brand.brand_name.IndexOf(brandName, 0, StringComparison.CurrentCultureIgnoreCase) != -1)
                    {
                        cproducts.Add(new CInventory(i, new CProduct(p, type, brand, uom, 0)));
                    }
                }
            }
            return Ok(cproducts);

        }

        [HttpGet]
        [ActionName("SearchByModel")]
        public IHttpActionResult SearchModel(string apiKey, string model)
        {
            SMS_DBEntities1 db = new SMS_DBEntities1();
            var user = db.users.FirstOrDefault(y => y.api_key.Equals(apiKey));
            var shop = db.shops.FirstOrDefault(y => y.shop_id == user.shop_id);
            var inventory = db.inventories.Where(y => y.shop_id == shop.shop_id && !(y.is_brand_active.Equals("N") || y.is_prod_active.Equals("N")));

            var cproducts = new List<CInventory>();
            foreach (var i in inventory)
            {
                var p = db.products.FirstOrDefault(y => y.product_id == i.product_id);
                if (null != p)
                {
                    var type = db.product_types.FirstOrDefault(y => y.type_id == p.product_type);
                    var brand = db.brands.FirstOrDefault(y => y.brand_id == p.brand_id);
                    var uom = db.msrmnt_units.FirstOrDefault(y => y.sr_no == p.unit_of_msrmnt);
                    if (p.product_name.IndexOf(model, 0, StringComparison.CurrentCultureIgnoreCase) != -1)
                    {
                        cproducts.Add(new CInventory(i, new CProduct(p, type, brand, uom, 0)));
                    }
                }
            }
            return Ok(cproducts);

        }

        [HttpGet]
        [ActionName("getMeasurementUnits")]
        public IHttpActionResult getMeasurementUnits(string apiKey)
        {
            SMS_DBEntities1 db = new SMS_DBEntities1();
            var user = db.users.FirstOrDefault(y => y.api_key.Equals(apiKey));
            var shop = db.shops.FirstOrDefault(y => y.shop_id == user.shop_id);
            HashSet<CUoM> list = new HashSet<CUoM>();

            foreach (var inventory in shop.inventories)
                list.Add(new CUoM(inventory.product.msrmnt_units));

            return Ok(list.ToArray());
        }

        [HttpGet]
        [ActionName("getBrandsByShop")]
        public IHttpActionResult getBrandsByShop(string userId)
        {
            SMS_DBEntities1 db = new SMS_DBEntities1();
            var user = db.users.FirstOrDefault(y => y.user_id.Equals(userId));
            var shop = db.shops.FirstOrDefault(y => y.shop_id == user.shop_id);

            //db.inventories.Where(y=>y.shop_id)
            List<JBrand> brands = new List<JBrand>();
            foreach (var inventory in shop.inventories.Where(y => y.is_brand_active.Equals("Y")))
            {
                brands.Add(new JBrand
                {
                    brand_name = inventory.product.brand.brand_name,
                    brand_id = inventory.product.brand.brand_id
                });
            }

            return Ok(brands);
        }

        [HttpGet]
        [ActionName("getProductTypeByShop")]
        public IHttpActionResult getProductTypeByShop(string userId)
        {
            SMS_DBEntities1 db = new SMS_DBEntities1();
            var user = db.users.FirstOrDefault(y => y.user_id.Equals(userId));
            var shop = db.shops.FirstOrDefault(y => y.shop_id == user.shop_id);

            //db.inventories.Where(y=>y.shop_id)
            List<JProductType> pTypes = new List<JProductType>();
            foreach (var inventory in shop.inventories)
            {
                pTypes.Add(new JProductType
                {
                    type_name = inventory.product.product_types.type_name,
                    type_id = inventory.product.product_types.type_id
                });
            }

            return Ok(pTypes);
        }

        [HttpGet]
        [ActionName("getUsersByShop")]
        public IHttpActionResult getUsersByShop(string apiKey)
        {
            SMS_DBEntities1 db = new SMS_DBEntities1();
            List<CUser> users = new List<CUser>();
            var user = db.users.FirstOrDefault(y => y.api_key.Equals(apiKey));
            if(null != user && user.role_id.Equals("admin"))
            {
                var userList = db.users.Where(x => x.shop_id == user.shop_id);
                if (userList != null)
                {
                    foreach (var u in userList)
                        users.Add(new CUser(u));
             
                }
            }
            return Ok(users);
        }


    }
}
