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
    
    public partial class purchase
    {
        public int sr_no { get; set; }
        public int purch_id { get; set; }
        public int prod_id { get; set; }
        public int prod_quant { get; set; }
        public int shop_id { get; set; }
        public string is_pmnt_clr { get; set; }
        public string is_invoice { get; set; }
        public Nullable<double> total_amt { get; set; }
        public Nullable<double> paid_amt { get; set; }
        public string agent_id { get; set; }
        public string dlr_name { get; set; }
        public string dlr_phone { get; set; }
        public System.DateTime pur_date { get; set; }
        public System.TimeSpan pur_time { get; set; }
        public string dlr_dtls { get; set; }
    
        public virtual product product { get; set; }
        public virtual shop shop { get; set; }
        public virtual user user { get; set; }
    }
}
