using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DAL.TimeUuidGenerator
{
    public enum GuidVersion
    {
        TimeBased = 0x01,
        Reserved = 0x02,
        NameBased = 0x03,
        Random = 0x04
    }
}
