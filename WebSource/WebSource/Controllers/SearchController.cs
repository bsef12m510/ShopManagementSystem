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
            var user = db.users.FirstOrDefault(y => y.user_id.Equals(user_id));
            var shop = db.shops.FirstOrDefault(y => y.shop_id == user.shop_id);
            var inventory = db.inventories.Where(y => y.shop_id == shop.shop_id);
            
            
            //var inventory = db.inventories;



            //foreach (var i in inventory)
            //{
            //    var p = db.products.first(y => y.product_id == i.product_id);
            //    if (db.product_types.first(y=>y.type_id==p.product_type).type_name.equals(product))
            //        products.add(p);
                
            //}
           // return Ok(products);

            var cproducts = new List<CProduct>();
            foreach (var i in inventory)
            {
                var p = db.products.FirstOrDefault(y => y.product_id == i.product_id);
                var type = db.product_types.FirstOrDefault(y => y.type_id == p.product_type);
                var brand = db.brands.FirstOrDefault(y => y.brand_id == p.brand_id);
                if (type.type_name.Equals(product)) {     
                    cproducts.Add(new CProduct(p,type,brand,i.prod_quant));
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
            var inventory = db.inventories.Where(y => y.shop_id == shop.shop_id);

            var cproducts = new List<CProduct>();
            foreach (var i in inventory)
            {
                var p = db.products.FirstOrDefault(y => y.product_id == i.product_id);
                var type = db.product_types.FirstOrDefault(y => y.type_id == p.product_type);
                var brand = db.brands.FirstOrDefault(y => y.brand_id == p.brand_id);
                if (brand.brand_name.Equals(brandName))
                {
                    cproducts.Add(new CProduct(p, type, brand, i.prod_quant));
                }

            }
            return Ok(cproducts);

        }

        [HttpGet]
        [ActionName("getMeasurementUnits")]
        public IHttpActionResult getMeasurementUnits(string userId)
        {
            SMS_DBEntities1 db = new SMS_DBEntities1();
            var user = db.users.FirstOrDefault(y => y.user_id.Equals(userId));
            var shop = db.shops.FirstOrDefault(y => y.shop_id == user.shop_id);
            
            return Ok(db.msrmnt_units.ToArray());
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
            foreach (var inventory in shop.inventories)
            {
                brands.Add(new JBrand { brand_name = inventory.product.brand.brand_name,
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

    }
}
