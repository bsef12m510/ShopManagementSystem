using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WebSource.Models
{
    public class CUoM
    {
        public int sr_no { get; set; }
        public string description { get; set; }
        public CUoM(msrmnt_units u)
        {
            sr_no = u.sr_no;
            description = u.description;
        }

        public override bool Equals(object obj)
        {
            CUoM q = obj as CUoM;
            return q != null && q.sr_no == this.sr_no;
        }

        public override int GetHashCode()
        {
            return this.sr_no.GetHashCode();
        }
    }
}