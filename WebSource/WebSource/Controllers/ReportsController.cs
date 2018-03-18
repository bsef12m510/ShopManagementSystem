using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using WebSource.Models;

namespace WebSource.Controllers
{
    public class ReportsController : ApiController
    {
        [HttpGet]
        [ActionName("stockReport")]
        public IHttpActionResult GetStockReport(string apiKey)
        {
            SMS_DBEntities1 db = new SMS_DBEntities1();
            var user = db.users.FirstOrDefault(x => x.api_key.Equals(apiKey));
            if(null != user)
            {
                var shop = db.shops.FirstOrDefault(x => x.shop_id == user.shop_id);
                if(null != shop)
                {
                    var inventory = db.inventories.Where(x => x.shop_id == shop.shop_id);
                    if(null != inventory)
                    {
                        var cproducts = new List<CProduct>();
                        foreach(var p in inventory)
                        {
                            var product = db.products.FirstOrDefault(x => x.product_id == p.product_id);
                            var product_type = db.product_types.FirstOrDefault(x => x.type_id == product.product_type);
                            var brand = db.brands.FirstOrDefault(x => x.brand_id == product.brand_id);
                            cproducts.Add(new CProduct(product,product_type,brand,p.prod_quant));
                        }

                        return Ok(cproducts);
                    }
                }
            }
            return BadRequest();
        }


        [HttpGet]
        [ActionName("salesReport")]
        public IHttpActionResult GetSalesReport(string apiKey, DateTime dateFrom, DateTime dateTo)
        {
            SMS_DBEntities1 db = new SMS_DBEntities1();
            var user = db.users.FirstOrDefault(x => x.api_key.Equals(apiKey));
            if (null != user)
            {
                var shop = db.shops.FirstOrDefault(x => x.shop_id == user.shop_id);
                if (null != shop)
                {
                    var csales = new List<CSale>();
                    var sales = db.sales.Where(x => x.shop_id == shop.shop_id);
                    foreach(var s in sales)
                    {
                        var product = db.products.FirstOrDefault(x => x.product_id == s.product_id);
                        var product_type = db.product_types.FirstOrDefault(x => x.type_id == product.product_type);
                        var brand = db.brands.FirstOrDefault(x => x.brand_id == product.brand_id);
                        var cproduct = new CProduct(product, product_type, brand, 0);
                        var agent = db.users.FirstOrDefault(x => x.user_id.Equals(s.agent_id));
                        var cuser = new CUser(agent);

                        csales.Add(new CSale(s, cproduct, cuser));
                    }

                    return Ok(csales);
                }
                    
            }
            return BadRequest();
        }
    }
}
