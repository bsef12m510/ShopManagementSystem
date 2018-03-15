using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WebSource.Models
{
    public class CPurchase
    {
        public CProduct product { get; set; }
        public string is_pmnt_clr { get; set; }
        public Nullable<double> total_amt { get; set; }
        public Nullable<double> paid_amt { get; set; }
        public string agent_id { get; set; }
        public string dlr_name { get; set; }
        public string dlr_phone { get; set; }      
    }
}