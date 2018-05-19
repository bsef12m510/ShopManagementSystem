using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using WebSource.Models;
using System.Globalization;
using System.Data.SqlClient;

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
            if (null != user)
            {
                var shop = db.shops.FirstOrDefault(x => x.shop_id == user.shop_id);
                if (null != shop)
                {
                    var inventory = db.inventories.Where(x => x.shop_id == shop.shop_id);
                    if (null != inventory)
                    {
                        var cproducts = new List<CProduct>();
                        foreach (var p in inventory)
                        {
                            var product = db.products.FirstOrDefault(x => x.product_id == p.product_id);
                            if (null != product)
                            {
                                var product_type = db.product_types.FirstOrDefault(x => x.type_id == product.product_type);
                                var brand = db.brands.FirstOrDefault(x => x.brand_id == product.brand_id);
                                var uom = db.msrmnt_units.FirstOrDefault(y => y.sr_no == product.unit_of_msrmnt);
                                cproducts.Add(new CProduct(product, product_type, brand, uom, p.prod_quant));
                            }
                        }

                        return Ok(cproducts);
                    }
                }
            }
            return BadRequest();
        }

        [HttpGet]
        [ActionName("stockReportByDate")]
        public IHttpActionResult GetStockReportByDate(string apiKey, string dateFromString, string dateToString, int productTypeId, int brandId, string agentId)
        {
            var cproductsInventory = new List<CProduct>();
            DateTime dateToday = DateTime.Now.Date;
            SMS_DBEntities1 db = new SMS_DBEntities1();
            var user = db.users.FirstOrDefault(x => x.api_key.Equals(apiKey));
            if (null != user && user.role_id.Equals("admin"))
            {
                var shop = db.shops.FirstOrDefault(x => x.shop_id == user.shop_id);
                if (null != shop)
                {
                    var inventory = db.inventories.Where(x => x.shop_id == shop.shop_id && !(x.is_brand_active.Equals("N") || x.is_prod_active.Equals("N")));
                    if (null != inventory)
                    {
                        var productsMap = new Dictionary<int, CProduct>();
                        var cproductsSales = new List<CProduct>();
                        IEnumerable<CProduct> distinctList = null;
                        IEnumerable<sale> sales;

                        foreach (var p in inventory)
                        {
                            var product = db.products.FirstOrDefault(x => x.product_id == p.product_id &&
                                                                     ((brandId != (-1)) ? (x.brand_id == brandId) : true) &&
                                                                     ((productTypeId != (-1)) ? (x.product_type == productTypeId) : true));
                            if (null != product)
                            {
                                var product_type = db.product_types.FirstOrDefault(x => x.type_id == product.product_type);
                                var brand = db.brands.FirstOrDefault(x => x.brand_id == product.brand_id);
                                var uom = db.msrmnt_units.FirstOrDefault(y => y.sr_no == product.unit_of_msrmnt);
                                var cp = new CProduct(product, product_type, brand, uom, p.prod_quant);
                                if (!productsMap.ContainsKey(cp.product_id))
                                    productsMap[cp.product_id] = cp;

                                cproductsInventory.Add(cp);
                            }
                        }
                        if (null != dateFromString && dateFromString != "" && null != dateToString && dateToString != "")
                        {
                            DateTime dateFrom = DateTime.ParseExact(dateFromString, "d", CultureInfo.CreateSpecificCulture("en-US"));
                            DateTime dateTo = DateTime.ParseExact(dateToString, "d", CultureInfo.CreateSpecificCulture("en-US"));
                            if (DateTime.Compare(dateFrom, dateTo) == 0)
                                sales = db.sales.Where(x => x.shop_id == shop.shop_id && DbFunctions.TruncateTime(x.sale_date) == dateFrom.Date &&
                                                        (agentId != null ? x.agent_id.Equals(agentId) : true));
                            else
                                sales = db.sales.Where(x => x.shop_id == shop.shop_id && DbFunctions.TruncateTime(x.sale_date) >= dateFrom.Date && DbFunctions.TruncateTime(x.sale_date) <= dateTo.Date
                                                        && ((agentId != null && !agentId.Equals("")) ? x.agent_id.Equals(agentId) : true));

                        }
                        else
                        {
                            sales = db.sales.Where(x => x.shop_id == shop.shop_id &&
                                                       (agentId != null ? x.agent_id.Equals(agentId) : true));
                        }
                        // var sales = db.sales.Where(x => x.shop_id == shop.shop_id && x.sale_date.Date == dateToday);
                        if (sales == null || sales.Count() == 0)
                            return Ok(new List<CProduct>());

                        foreach (var s in sales)
                        {
                            var product = db.products.FirstOrDefault(x => x.product_id == s.product_id &&
                                                                     ((brandId != (-1)) ? (x.brand_id == brandId) : true) &&
                                                                     ((productTypeId != (-1)) ? (x.product_type == productTypeId) : true));
                            if (null != product)
                            {
                                var type = db.product_types.FirstOrDefault(y => y.type_id == product.product_type);
                                var brand = db.brands.FirstOrDefault(y => y.brand_id == product.brand_id);
                                var uom = db.msrmnt_units.FirstOrDefault(y => y.sr_no == product.unit_of_msrmnt);
                                var usr = db.users.FirstOrDefault(y => y.user_id.Equals(s.agent_id));
                                var cp = new CProduct(product, type, brand, uom, s.prod_quant, usr);
                                if (productsMap.ContainsKey(cp.product_id))
                                {
                                    if (null != productsMap[cp.product_id].otherThanCurrentInventoryQty)
                                        productsMap[cp.product_id].otherThanCurrentInventoryQty += s.prod_quant; // how much sold in given time
                                    else
                                        productsMap[cp.product_id].otherThanCurrentInventoryQty = s.prod_quant;
                                    productsMap[cp.product_id].user = new CUser(usr);
                                }
                                cproductsSales.Add(cp);
                            }

                        }

                        distinctList = cproductsSales.GroupBy(x => x.product_id).Select(x => x.First());
                        foreach (var p in cproductsInventory)
                        {
                            p.qty = productsMap[p.product_id].qty;
                            p.otherThanCurrentInventoryQty = productsMap[p.product_id].otherThanCurrentInventoryQty;
                            p.user = productsMap[p.product_id].user;
                        }
                        distinctList = distinctList.OrderBy(p => p.qty);
                        return Ok(cproductsInventory);
                    }
                }
            }

            return Ok(cproductsInventory);
        }


        [HttpGet]
        [ActionName("salesReport")]
        public IHttpActionResult GetSalesReport(string apiKey, string dateFromString, string dateToString, int productTypeId, int brandId, string agentId)
        {

            SMS_DBEntities1 db = new SMS_DBEntities1();
            var user = db.users.FirstOrDefault(x => x.api_key.Equals(apiKey));
            if (null != user)
            {
                var shop = db.shops.FirstOrDefault(x => x.shop_id == user.shop_id);
                if (null != shop)
                {
                    var csales = new List<CSale>();
                    IEnumerable<sale> sales;
                    if (null != dateFromString && null != dateToString)
                    {
                        DateTime dateFrom = DateTime.ParseExact(dateFromString, "d", CultureInfo.CreateSpecificCulture("en-US"));
                        DateTime dateTo = DateTime.ParseExact(dateToString, "d", CultureInfo.CreateSpecificCulture("en-US"));

                        if (DateTime.Compare(dateFrom, dateTo) == 0)
                            sales = db.sales.Where(x => x.shop_id == shop.shop_id && x.sale_date == dateFrom);
                        else
                            sales = db.sales.Where(x => x.shop_id == shop.shop_id && x.sale_date >= dateFrom && x.sale_date <= dateTo);
                    }
                    else
                    {
                        sales = db.sales.Where(x => x.shop_id == shop.shop_id &&
                                                   (agentId != null ? x.agent_id.Equals(agentId) : true));
                    }
                    foreach (var s in sales)
                    {
                        var product = db.products.FirstOrDefault(x => x.product_id == s.product_id &&
                                                                     ((brandId != (-1)) ? (x.brand_id == brandId) : true) &&
                                                                     ((productTypeId != (-1)) ? (x.product_type == productTypeId) : true));
                        if (null != product)
                        {
                            var product_type = db.product_types.FirstOrDefault(x => x.type_id == product.product_type);
                            var brand = db.brands.FirstOrDefault(x => x.brand_id == product.brand_id);
                            var uom = db.msrmnt_units.FirstOrDefault(y => y.sr_no == product.unit_of_msrmnt);
                            var cproduct = new CProduct(product, product_type, brand, uom, 0);
                            var agent = db.users.FirstOrDefault(x => x.user_id.Equals(s.agent_id));
                            var cuser = new CUser(agent);

                            csales.Add(new CSale(s, cproduct, cuser));
                        }
                    }

                    return Ok(csales);
                }

            }
            return BadRequest();

        }

        [HttpGet]
        [ActionName("GetSalesAmountByMonth")]
        public IHttpActionResult GetSalesAmountByMonth(string apiKey)
        {

            SMS_DBEntities1 db = new SMS_DBEntities1();
            var user = db.users.FirstOrDefault(x => x.api_key.Equals(apiKey));
            if (null != user)
            {
                var shop = db.shops.FirstOrDefault(x => x.shop_id == user.shop_id);
                if (null != shop)
                {
                    
                    var sales = db.Database.SqlQuery<SaleByMonth>("select DATEADD(DAY,1,EOMONTH(sale_date,-1)) as date,sum(paid_amt) as saleAmount from sales where shop_id = @shop and sale_date between DATEADD(DAY,-365,GETDATE()) and GETDATE() and is_invoice = 'Y' group by DATEADD(DAY,1,EOMONTH(sale_date,-1))", new SqlParameter("@shop",shop.shop_id));

                    return Ok(sales);
                }

            }
            return BadRequest();

        }

    }
}
