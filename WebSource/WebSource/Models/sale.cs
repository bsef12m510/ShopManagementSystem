//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated from a template.
//
//     Manual changes to this file may cause unexpected behavior in your application.
//     Manual changes to this file will be overwritten if the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

namespace WebSource.Models
{
    using System;
    using System.Collections.Generic;
    
    public partial class sale
    {
        public int sr_no { get; set; }
        public string sale_id { get; set; }
        public int product_id { get; set; }
        public Nullable<int> shop_id { get; set; }
        public int prod_quant { get; set; }
        public string is_pmnt_clr { get; set; }
        public string is_invoice { get; set; }
        public Nullable<double> total_amt { get; set; }
        public Nullable<double> paid_amt { get; set; }
        public Nullable<double> discount { get; set; }
        public string agent_id { get; set; }
        public string cust_name { get; set; }
        public string cust_phone { get; set; }
        public System.DateTime sale_date { get; set; }
        public System.TimeSpan sale_time { get; set; }
    
        public virtual product product { get; set; }
        public virtual shop shop { get; set; }
        public virtual user user { get; set; }
    }
}
