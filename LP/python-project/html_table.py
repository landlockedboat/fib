#!/usr/bin/python3


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
