#!/usr/bin/python3

import urllib.request
import xml.etree.ElementTree as ET
import argparse
from ast import literal_eval
from datetime import timedelta, datetime
from math import radians, cos, sin, asin, sqrt


def eval_conjunction(key, eval_string):
    for k in key:
        if not eval_key(k, eval_string):
            return False
    return True


def eval_disjunction(key, eval_string):
    for k in key:
        if eval_key(k, eval_string):
            return True
    return False


def eval_word(key, eval_string):
    return key in eval_string


def eval_key(key, eval_string):
    if isinstance(key, list):
        return eval_conjunction(key, eval_string)
    elif isinstance(key, tuple):
        return eval_disjunction(key, eval_string)
    elif isinstance(key, str):
        return eval_word(key, eval_string)
    else:
        print ('non valid key:')
        print (key)


def to_date(date_string):
    return datetime.strptime(date_string, '%d/%m/%Y')


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
        print ('invalid date:')
        print (date)


def find_text(item, attrib):
    try:
        itm_attrib = item.find(attrib)
    except AttributeError:
        return ""
    if itm_attrib is not None:
        attr_txt = itm_attrib.text
        if attr_txt is None:
            return ""
        else:
            return attr_txt
    else:
        return ""


def get_dist(lon1, lat1, lon2, lat2):
    lon1, lat1, lon2, lat2 = map(radians, [lon1, lat1, lon2, lat2])
    dlon = lon2 - lon1
    dlat = lat2 - lat1
    a = pow(sin(dlat/2), 2) + cos(lat1) * cos(lat2) * pow(sin(dlon/2), 2)
    c = 2 * asin(sqrt(a))
    m = 6367000 * c
    return m


def get_close_stats(stations, coordx, coordy):
    close_stats = {}
    for stat in stations:
        statx = float(find_text(stat, 'lat'))
        staty = float(find_text(stat, 'long'))
        s_dist = get_dist(statx, staty, coordx, coordy)
        if s_dist <= 500:
            close_stats[s_dist] = stat
    return close_stats


def get_close_parking(parkings, coordx, coordy):
    close_parks = {}
    for park in parkings:
        parkx = float(park.find('addresses/item/gmapx').text)
        parky = float(park.find('addresses/item/gmapy').text)
        s_dist = get_dist(parkx, parky, coordx, coordy)
        if s_dist <= 500:
            close_parks[s_dist] = park
    return close_parks
    pass


def print_bicing_parking(close_stats, output_list):
    output_list.append(["closestations"])
    output_list.append(["Station id", "Location", "Distance", "Free slots"])
    printed = 0
    for key in sorted(close_stats):
        stat = close_stats[key]
        slots = int(stat.find('slots').text)
        if slots > 0:
            col = []
            stat_id = stat.find('id').text
            address = stat.find('street').text
            address += ', ' + find_text(stat, 'streetNumber')
            col.append(stat_id)
            col.append(address)
            col.append('{:.2f}'.format(key) + "m")
            col.append(str(slots))
            output_list.append(col)
            printed += 1
            if printed >= 5:
                return


def print_bicing_bikes(close_stats, output_list):
    output_list.append(["closebikes"])
    output_list.append(["Station id", "Location", "Distance", "Free bikes"])
    printed = 0
    for key in sorted(close_stats):
        stat = close_stats[key]
        bikes = int(stat.find('bikes').text)
        if bikes > 0:
            col = []
            stat_id = stat.find('id').text
            address = stat.find('street').text
            address += ', ' + find_text(stat, "streetNumber")
            col.append(stat_id)
            col.append(address)
            col.append('{:.2f}'.format(key) + "m")
            col.append(str(bikes))
            output_list.append(col)
            printed += 1
            if printed >= 5:
                return


def print_public_parking(close_parking, output_list):
    output_list.append(["closeparking"])
    output_list.append(["Parking name",
                        "Address", "Distance"])
    printed = 0
    for key in sorted(close_parking):
        col = []
        park = close_parking[key]
        park_name = park.find('name').text
        park_address = park.find('addresses/item/address').text
        col.append(park_name)
        col.append(park_address)
        col.append('{:.2f}'.format(key) + 'm')
        output_list.append(col)
        printed += 1
        if printed >= 5:
            return


def print_event(event, stations, parkings, output_list):
    output_list.append(["Event name", "Event address",
                        "Event begin date", "Event end date",
                        "Event starting time"])
    fst_row = []
    event_name = event.find('name').text
    fst_row.append(event_name)

    addr = event.find('addresses/item')
    event_address = find_text(addr, 'address')
    fst_row.append(event_address)

    bgndate = event.find('begindate')
    enddate = event.find('enddate')
    if bgndate is not None and enddate is not None:
        fst_row.append(bgndate.text)
        fst_row.append(enddate.text)
    else:
        proxdate = event.find('proxdate')
        if proxdate is not None:
            fst_row.append(proxdate.text)
            fst_row.append(proxdate.text)

    proxhour = event.find('proxhour')
    if proxhour is not None:
        fst_row.append(proxhour.text)

    output_list.append(fst_row)

    coordx = find_text(addr, 'gmapx')
    coordy = find_text(addr, 'gmapy')

    if coordx is not "" and coordy is not "":
        coordx = float(coordx)
        coordy = float(coordy)
    else:
        coordx = 500.0
        coordy = 500.0

    close_stats = get_close_stats(stations, coordx, coordy)
    close_parking = get_close_parking(parkings, coordx, coordy)

    print_bicing_parking(close_stats, output_list)
    print_bicing_bikes(close_stats, output_list)
    print_public_parking(close_parking, output_list)


def eval_elem_date(item, date):
    bgndate = item.find('begindate')
    enddate = item.find('enddate')
    if bgndate is not None:
        if enddate is not None:
            return eval_date(date, bgndate.text, enddate.text)

    proxdate = item.find('proxdate')
    if proxdate is not None:
        return eval_date(date, proxdate.text, proxdate.text)
    return -1


# Activity name
# Location name / district name
def eval_elem_key(item, key):
    search_string = item.find('name').text
    addr = item.find('addresses/item')
    if addr is not None:
        dist = addr.find('district')
        if dist is not None:
            search_string += dist.text
        neigh = addr.find('barri')
        if neigh is not None:
            search_string += neigh.text
    return eval_key(key, search_string)


def main():
    parser = argparse.ArgumentParser(prog='cerca')
    parser.add_argument('-k', '--key', nargs=1,
                        help='search for certain words')
    parser.add_argument('-d', '--date', nargs=1,
                        help='limit your search to certain dates')

    args = vars(parser.parse_args())

    key = args['key']
    date = args['date']

    if key is not None:
        key = literal_eval(key[0])
    if date is not None:
        date = literal_eval(date[0])

    sock = urllib.request.urlopen(
        'http://www.bcn.cat/tercerlloc/agenda_cultural.xml')
    xmlSource = sock.read()
    sock.close()
    events_root = ET.fromstring(xmlSource)

    sock = urllib.request.urlopen(
        'http://wservice.viabicing.cat/getstations.php?v=1')
    xmlSource = sock.read()
    sock.close()
    bicing_root = ET.fromstring(xmlSource)

    sock = urllib.request.urlopen(
        'http://www.bcn.cat/tercerlloc/Aparcaments.xml')
    xmlSource = sock.read()
    sock.close()
    parking_root = ET.fromstring(xmlSource)

    parkings = parking_root.findall(
        './search/queryresponse/list/list_items/row/item')
    events = events_root.findall(
        './search/queryresponse/list/list_items/row/item')
    stations = bicing_root.findall("station")

    output_list = []

    if date is not None:
        prox_dict = {}
        for item in events:
            time_dist = eval_elem_date(item, date)
            if time_dist > -1:
                display = True
                if key is not None:
                    display = eval_elem_key(item, key)
                if display:
                    prox_dict[time_dist] = item

        for key in sorted(prox_dict):
            print_event(item, stations, parkings, output_list)
    else:
        for item in events:
            display = True
            if key is not None:
                display = eval_elem_key(item, key)
            if display:
                print_event(item, stations, parkings, output_list)
    write_html(output_list)


def write_html(output_list):
    print("<!DOCTYPE html>")
    print("<html>")
    print("<head> <meta charset='UTF-8'> </head>")
    print("<body>")
    print("<table>")

    bold_effect = False
    set_bold_effect = False
    for row in output_list:
        print("<tr>")
        for elem in row:
            if elem is "closestations":
                print("<td colspan='5' bgcolor='WhiteSmoke'>")
                print("Bicing stations close to the event that have " +
                      "free bike slots")
                set_bold_effect = True
            elif elem is "closebikes":
                print("<td colspan='5' bgcolor='WhiteSmoke'>")
                print("Bicing stations close to the event that have " +
                      "free bikes")
                set_bold_effect = True
            elif elem is "closeparking":
                print("<td colspan='5' bgcolor='WhiteSmoke'>")
                print("Public parking near the event location")
                set_bold_effect = True
            elif "Event" in elem:
                print("<td bgcolor='Silver'>")
                print("<b>")
                print(elem)
                print("</b>")
            elif bold_effect:
                print("<td>")
                print("<b>")
                print(elem)
                print("</b>")
            else:
                print("<td>")
                print(elem)
            print("</td>")
        print("</tr>")

        bold_effect = False
        if set_bold_effect:
            bold_effect = True
            set_bold_effect = False
    print("</table>")
    print("</body>")
    print("</html>")


if __name__ == '__main__':
    main()
