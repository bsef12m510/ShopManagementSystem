using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Data.Entity.Core.Objects;
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
        [ActionName("GetAllProductTypes")]
        public IHttpActionResult GetAllProductTypes(String apiKey)
        {
            var prodTypeList = new List<CProductType>();
            IEnumerable<CProductType> distinctList = null;
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
                    prodTypeList.Add(new CProductType(type));
                    // var brand = db.brands.FirstOrDefault(y => y.brand_id == p.brand_id);
                    // cinventory.Add(new CInventory(i, new CProduct(p, type, brand, 0)));
                }

                distinctList = prodTypeList.GroupBy(x => x.type_id).Select(x => x.First());

                return Ok(distinctList);
            }
            else
            {
                return BadRequest();
            }
        }

        [HttpGet]
        [ActionName("GetAllModelsForProductType")]
        public IHttpActionResult GetAllModelsForProductTypeAndBrand(String apiKey, int productTypeId, int brandId)
        {
            SMS_DBEntities1 db = new SMS_DBEntities1();
            var user = db.users.FirstOrDefault(x => x.api_key.Equals(apiKey));
            if (null != user)
            {
                var shop = db.shops.FirstOrDefault(x => x.shop_id == user.shop_id);
                var inventory = db.inventories.Where(x => x.shop_id == shop.shop_id && x.is_prod_active.Equals("Y")
                                                                                    && x.is_brand_active.Equals("Y"));
                var cbrands = new Dictionary<int, CBrand>();
                var cproducts = new List<CProduct>();
                IEnumerable<CProduct> distinctList = null;
                foreach (inventory i in inventory)
                {
                    var p = db.products.FirstOrDefault(y => y.product_id == i.product_id && y.product_type == productTypeId && y.brand_id == brandId);
                    if (null != p)
                    {
                        var uom = db.msrmnt_units.FirstOrDefault(y => y.sr_no == p.unit_of_msrmnt);

                        cproducts.Add(new CProduct(p, null, null, uom, 0));
                    }
                }

                return Ok(cproducts);
            }


            return BadRequest();

        }

        [HttpGet]
        [ActionName("GetAllProducts")]
        public IHttpActionResult GetAllProducts(String apiKey)
        {
            SMS_DBEntities1 db = new SMS_DBEntities1();
            var user = db.users.FirstOrDefault(x => x.api_key.Equals(apiKey));
            if (null != user)
            {
                var shop = db.shops.FirstOrDefault(x => x.shop_id == user.shop_id);
                var inventory = db.inventories.Where(x => x.shop_id == shop.shop_id && x.is_prod_active.Equals("Y"));
                var cinventory = new List<CInventory>();
                foreach (inventory i in inventory)
                {
                    var p = db.products.FirstOrDefault(y => y.product_id == i.product_id);
                    if (null != p)
                    {
                        var type = db.product_types.FirstOrDefault(y => y.type_id == p.product_type);
                        var brand = db.brands.FirstOrDefault(y => y.brand_id == p.brand_id);
                        var uom = db.msrmnt_units.FirstOrDefault(y => y.sr_no == p.unit_of_msrmnt);
                        cinventory.Add(new CInventory(i, new CProduct(p, type, brand, uom, 0)));
                    }
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
                var inventory = db.inventories.Where(x => x.shop_id == shop.shop_id && x.is_brand_active.Equals("Y"));
                var cbrands = new Dictionary<int, CBrand>();
                var cbrands1 = new List<CBrand>();
                IEnumerable<CBrand> distinctList = null;
                foreach (inventory i in inventory)
                {
                    var p = db.products.FirstOrDefault(y => y.product_id == i.product_id && y.product_type == productTypeId);
                    if (null != p)
                    {
                        var brand = db.brands.FirstOrDefault(y => y.brand_id == p.brand_id);
                        //  if(!cbrands.ContainsKey(brand.brand_id))
                        //    cbrands[brand.brand_id] = new CBrand(brand);
                        if (brand != null)
                        {
                            cbrands1.Add(new CBrand(brand));
                            distinctList = cbrands1.GroupBy(x => x.brand_id).Select(x => x.First());
                        }
                    }
                }

                return Ok(distinctList);
            }


            return BadRequest();

        }

        [HttpDelete]
        [ActionName("DeleteModel")]
        public IHttpActionResult Delete(String apiKey, int id)
        {
            SMS_DBEntities1 db = new SMS_DBEntities1();
            var user = db.users.FirstOrDefault(x => x.api_key.Equals(apiKey));
            //if (user.role_id.Equals("admin"))
            //{

            var shop = db.shops.FirstOrDefault(x => x.shop_id == user.shop_id);
            var inventory = db.inventories.Where(x => x.shop_id == shop.shop_id);

            foreach (inventory i in inventory)
            {
                var p = db.products.FirstOrDefault(y => y.product_id == i.product_id);
                if (null != p && p.product_id == id)
                {
                    //inventory.Remove(shop);
                    db.Entry(p).State = System.Data.Entity.EntityState.Deleted;
                    db.Entry(i).State = System.Data.Entity.EntityState.Deleted;
                    db.SaveChanges();
                    return Ok(new CProduct(p, null, null, null, 0));
                }

            }
            return Ok(-1);

            // }
            // else
            // {
            //     return BadRequest();
            // }
        }


        [HttpGet]
        [ActionName("GetTopSellingProducts")]
        public IHttpActionResult GetTopSellingProducts(String apiKey)
        {
            DateTime dateTo = DateTime.Now;
            DateTime dateFrom = new DateTime(dateTo.Year, dateTo.Month, 1);
            SMS_DBEntities1 db = new SMS_DBEntities1();
            var user = db.users.FirstOrDefault(x => x.api_key.Equals(apiKey));
            if (null != user && user.role_id.Equals("admin"))
            {
                var productsMap = new Dictionary<int, CProduct>();
                var cproducts = new List<CProduct>();
                IEnumerable<CProduct> distinctList = null;
                var shop = db.shops.FirstOrDefault(x => x.shop_id == user.shop_id);
                if (null != shop)
                {
                    var sales = db.sales.Where(x => x.shop_id == shop.shop_id && x.sale_date >= dateFrom && x.sale_date <= dateTo);
                    foreach (var s in sales)
                    {
                        var product = db.products.FirstOrDefault(x => x.product_id == s.product_id);
                        if (null != product)
                        {
                            var type = db.product_types.FirstOrDefault(y => y.type_id == product.product_type);
                            var brand = db.brands.FirstOrDefault(y => y.brand_id == product.brand_id);
                            var uom = db.msrmnt_units.FirstOrDefault(y => y.sr_no == product.unit_of_msrmnt);
                            var cp = new CProduct(product, type, brand, uom, s.prod_quant);

                            if (!productsMap.ContainsKey(cp.product_id))
                                productsMap[cp.product_id] = cp;
                            else
                                productsMap[cp.product_id].qty += s.prod_quant;

                            cproducts.Add(cp);
                        }

                    }

                    distinctList = cproducts.GroupBy(x => x.product_id).Select(x => x.First());
                    foreach (var p in distinctList)
                    {
                        p.qty = productsMap[p.product_id].qty;
                    }
                    distinctList = distinctList.OrderByDescending(p => p.qty);
                    return Ok(distinctList);
                }
            }
            return BadRequest();
        }


        [HttpGet]
        [ActionName("GetLowStockProducts")]
        public IHttpActionResult GetLowStockProducts(String apiKey)
        {
            SMS_DBEntities1 db = new SMS_DBEntities1();
            var user = db.users.FirstOrDefault(x => x.api_key.Equals(apiKey));
            if (null != user)
            {
                var shop = db.shops.FirstOrDefault(x => x.shop_id == user.shop_id);
                var inventory = db.inventories.Where(x => x.shop_id == shop.shop_id && x.is_prod_active.Equals("Y")
                                                                                    && x.is_brand_active.Equals("Y"));
                var cproducts = new List<CProduct>();
                foreach (var i in inventory)
                {
                    var p = db.products.FirstOrDefault(y => y.product_id == i.product_id);
                    if (null != p)
                    {
                        var type = db.product_types.FirstOrDefault(y => y.type_id == p.product_type);
                        var brand = db.brands.FirstOrDefault(y => y.brand_id == p.brand_id);
                        var uom = db.msrmnt_units.FirstOrDefault(y => y.sr_no == p.unit_of_msrmnt);
                        if (i.prod_quant <= 10)
                            cproducts.Add(new CProduct(p, type, brand, uom, i.prod_quant));
                    }
                }
                IEnumerable<CProduct> list = cproducts.OrderBy(x => x.qty);
                return Ok(list);
            }
            else
            {
                return BadRequest();
            }
        }


        [HttpGet]
        [ActionName("GetProductsSoldToday")]
        public IHttpActionResult GetProductsSoldToday(String apiKey)
        {
            DateTime dateToday = DateTime.Now.Date;

            SMS_DBEntities1 db = new SMS_DBEntities1();
            var user = db.users.FirstOrDefault(x => x.api_key.Equals(apiKey));
            if (null != user && user.role_id.Equals("admin"))
            {
                var shop = db.shops.FirstOrDefault(x => x.shop_id == user.shop_id);
                if (shop != null)
                {
                    var productsMap = new Dictionary<int, CProduct>();
                    var cproducts = new List<CProduct>();
                    IEnumerable<CProduct> distinctList = null;
                    var sales = db.sales.Where(x => x.shop_id == shop.shop_id && DbFunctions.TruncateTime(x.sale_date) == dateToday);
                    foreach (var s in sales)
                    {
                        var product = db.products.FirstOrDefault(x => x.product_id == s.product_id);
                        if (null != product)
                        {
                            var type = db.product_types.FirstOrDefault(y => y.type_id == product.product_type);
                            var brand = db.brands.FirstOrDefault(y => y.brand_id == product.brand_id);
                            var uom = db.msrmnt_units.FirstOrDefault(y => y.sr_no == product.unit_of_msrmnt);
                            var cp = new CProduct(product, type, brand, uom, s.prod_quant);

                            if (!productsMap.ContainsKey(cp.product_id))
                                productsMap[cp.product_id] = cp;
                            else
                                productsMap[cp.product_id].qty += s.prod_quant;

                            cproducts.Add(cp);
                        }
                    }

                    distinctList = cproducts.GroupBy(x => x.product_id).Select(x => x.First());
                    foreach (var p in distinctList)
                    {
                        p.qty = productsMap[p.product_id].qty;
                    }
                    distinctList = distinctList.OrderByDescending(p => p.qty);
                    return Ok(distinctList);
                }

            }
            return BadRequest();
        }

        [HttpGet]
        [ActionName("getInventory")]
        public IHttpActionResult getInventory(string apiKey)
        {
            var cproducts = new List<CProduct>();
            SMS_DBEntities1 db = new SMS_DBEntities1();
            var user = db.users.FirstOrDefault(y => y.api_key.Equals(apiKey));
            if (null != user)
            {
                var shop = db.shops.FirstOrDefault(x => x.shop_id == user.shop_id);
                var inventory = db.inventories.Where(x => x.shop_id == shop.shop_id && x.is_prod_active.Equals("Y")
                                                                                    && x.is_brand_active.Equals("Y"));

                foreach (var i in inventory)
                {
                    var p = db.products.FirstOrDefault(y => y.product_id == i.product_id);
                    if (null != p)
                    {
                        var type = db.product_types.FirstOrDefault(y => y.type_id == p.product_type);
                        var brand = db.brands.FirstOrDefault(y => y.brand_id == p.brand_id);
                        var uom = db.msrmnt_units.FirstOrDefault(y => y.sr_no == p.unit_of_msrmnt);

                        cproducts.Add(new CProduct(p, type, brand, uom, i.prod_quant));
                    }
                }
                IEnumerable<CProduct> list = cproducts.OrderBy(x => x.qty);
                return Ok(cproducts);
            }
            else
            {
                return Ok(cproducts);
            }
        }

        [HttpGet]
        [ActionName("getProductQuantity")]
        public IHttpActionResult getProductQuantity(string apiKey, int product)
        {
            var cproducts = new List<CProduct>();
            SMS_DBEntities1 db = new SMS_DBEntities1();
            var user = db.users.FirstOrDefault(y => y.api_key.Equals(apiKey));
            if (null != user)
            {
                var shop = db.shops.FirstOrDefault(x => x.shop_id == user.shop_id);
                var inv = shop.inventories.FirstOrDefault(y=>y.product_id == product);
                if (inv != null)
                    return Ok(inv.prod_quant);
            }

            return Ok(0);
        }
    }

}
