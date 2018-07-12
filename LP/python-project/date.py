#!/usr/bin/python3
from datetime import timedelta, datetime


def to_date(date_string):
    return datetime.strptime(date_string, "%d/%m/%Y")


# test if date1 ^ date2 is non-empty
def in_date(date1, date1_ini, date1_end, date2_ini, date2_end):
    if (date1_ini <= date2_end and date1_end >= date2_ini) or \
            (date2_ini <= date1_end and date2_end >= date1_ini):

        if date1 < date2_ini:
            return (date2_ini - date1).total_seconds()
        if date1 > date2_end:
            return (date1 - date2_end).total_seconds()
        return 0
    return -1


def eval_date(date, date_string_ini, date_string_end):
    date2_ini = to_date(date_string_ini)
    date2_end = to_date(date_string_end)
    if isinstance(date, tuple):
        date1 = to_date(date[0])
        date1_ini = date1 + timedelta(days=date[1])
        date1_end = date1 + timedelta(days=date[2])
        return in_date(date1, date1_ini, date1_end,
                       date2_ini, date2_end)
    elif isinstance(date, str):
        date1 = to_date(date)
        date1_ini = date1
        date1_end = date1
        return in_date(date1, date1_ini, date1_end,
                       date2_ini, date2_end)
    else:
        print ("invalid date:")
        print (date)


def _test_dates():
    print (eval_date(("14/01/1999", 0, 1), "15/01/1999", "31/01/1999"))
