package com.easylabel;

import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.easylabel.model.OrderData;
import com.easylabel.model.AddressData;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class PlentyApiClient {
    private final String apiUrl, username, password;
    private final CloseableHttpClient httpClient;
    private final ObjectMapper mapper = new ObjectMapper();
    private String accessToken;

    /** Numerische Plenty countryId → Land-Name (IDs 1–265) */
    public static final Map<Integer,String> PLE_COUNTRY_MAP;
    static {
        Map<Integer,String> m = new LinkedHashMap<>();
        m.put(1,   "Germany");
        m.put(2,   "Austria");
        m.put(3,   "Belgium");
        m.put(4,   "Switzerland");
        m.put(5,   "Cyprus");
        m.put(6,   "Czech Republic");
        m.put(7,   "Denmark");
        m.put(8,   "Spain");
        m.put(9,   "Estonia");
        m.put(10,  "France");
        m.put(11,  "Finland");
        m.put(12,  "United Kingdom");
        m.put(13,  "Greece");
        m.put(14,  "Hungary");
        m.put(15,  "Italy");
        m.put(16,  "Ireland");
        m.put(17,  "Luxembourg");
        m.put(18,  "Latvia");
        m.put(19,  "Malta");
        m.put(20,  "Norway");
        m.put(21,  "Netherlands");
        m.put(22,  "Portugal");
        m.put(23,  "Poland");
        m.put(24,  "Sweden");
        m.put(25,  "Singapore");
        m.put(26,  "Slovakia");
        m.put(27,  "Slovenia");
        m.put(28,  "USA");
        m.put(29,  "Australia");
        m.put(30,  "Canada");
        m.put(31,  "China");
        m.put(32,  "Japan");
        m.put(33,  "Lithuania");
        m.put(34,  "Liechtenstein");
        m.put(35,  "Monaco");
        m.put(36,  "Mexico");
        m.put(37,  "Canary Islands");
        m.put(38,  "India");
        m.put(39,  "Brazil");
        m.put(40,  "Russia");
        m.put(41,  "Romania");
        m.put(42,  "Ceuta");
        m.put(43,  "Melilla");
        m.put(44,  "Bulgaria");
        m.put(45,  "Kosovo");
        m.put(46,  "Kyrgyzstan");
        m.put(47,  "Kazakhstan");
        m.put(48,  "Belarus");
        m.put(49,  "Uzbekistan");
        m.put(50,  "Morocco");
        m.put(51,  "Armenia");
        m.put(52,  "Albania");
        m.put(53,  "Egypt");
        m.put(54,  "Croatia");
        m.put(55,  "Maldives");
        m.put(56,  "Malaysia");
        m.put(57,  "Hong Kong");
        m.put(58,  "Yemen");
        m.put(59,  "Israel");
        m.put(60,  "Taiwan");
        m.put(61,  "Guadeloupe");
        m.put(62,  "Thailand");
        m.put(63,  "Turkey");
        m.put(64,  "Greek Islands");
        m.put(65,  "Balearic Islands");
        m.put(66,  "New Zealand");
        m.put(67,  "Afghanistan");
        m.put(68,  "Åland Islands");
        m.put(69,  "Algeria");
        m.put(70,  "American Samoa");
        m.put(71,  "Andorra");
        m.put(72,  "Angola");
        m.put(73,  "Anguilla");
        m.put(74,  "Antarctica");
        m.put(75,  "Antigua and Barbuda");
        m.put(76,  "Argentina");
        m.put(77,  "Aruba");
        m.put(78,  "Azerbaijan");
        m.put(79,  "The Bahamas");
        m.put(80,  "Bahrain");
        m.put(81,  "Bangladesh");
        m.put(82,  "Barbados");
        m.put(83,  "Belize");
        m.put(84,  "Benin");
        m.put(85,  "Bermuda");
        m.put(86,  "Bhutan");
        m.put(87,  "Bolivia");
        m.put(88,  "Bosnia and Herzegovina");
        m.put(89,  "Botswana");
        m.put(90,  "Bouvet Island");
        m.put(91,  "British Indian Ocean Territory");
        m.put(92,  "Brunei Darussalam");
        m.put(93,  "Burkina Faso");
        m.put(94,  "Burundi");
        m.put(95,  "Cambodia");
        m.put(96,  "Cameroon");
        m.put(97,  "Cape Verde");
        m.put(98,  "Cayman Islands");
        m.put(99,  "Central African Republic");
        m.put(100, "Chad");
        m.put(101, "Chile");
        m.put(102, "Christmas Island");
        m.put(103, "Cocos Islands");
        m.put(104, "Colombia");
        m.put(105, "Comoros");
        m.put(106, "Congo");
        m.put(107, "Democratic Republic of the Congo");
        m.put(108, "Cook Islands");
        m.put(109, "Costa Rica");
        m.put(110, "Ivory Coast");
        m.put(112, "Cuba");
        m.put(113, "Djibouti");
        m.put(114, "Dominica");
        m.put(115, "Dominican Republic");
        m.put(116, "Ecuador");
        m.put(117, "El Salvador");
        m.put(118, "Equatorial Guinea");
        m.put(119, "Eritrea");
        m.put(120, "Ethiopia");
        m.put(121, "Falkland Islands");
        m.put(122, "Faroe Islands");
        m.put(123, "Fiji");
        m.put(124, "French Guiana");
        m.put(125, "French Polynesia");
        m.put(126, "French Southern and Antarctic Lands");
        m.put(127, "Gabon");
        m.put(128, "Gambia");
        m.put(129, "Georgia");
        m.put(130, "Ghana");
        m.put(131, "Gibraltar");
        m.put(132, "Greenland");
        m.put(133, "Grenada");
        m.put(134, "Guam");
        m.put(135, "Guatemala");
        m.put(136, "Guernsey");
        m.put(137, "Guinea");
        m.put(138, "Guinea-Bissau");
        m.put(139, "Guyana");
        m.put(140, "Haiti");
        m.put(141, "Heard Island and McDonald Islands");
        m.put(142, "Vatican City");
        m.put(143, "Honduras");
        m.put(144, "Iceland");
        m.put(145, "Indonesia");
        m.put(146, "Iran");
        m.put(147, "Iraq");
        m.put(148, "Isle of Man");
        m.put(149, "Jamaica");
        m.put(150, "Jersey");
        m.put(151, "Jordan");
        m.put(152, "Kenya");
        m.put(153, "Kiribati");
        m.put(154, "Democratic People’s Republic of Korea");
        m.put(155, "Republic of Korea");
        m.put(156, "Kuwait");
        m.put(158, "Laos");
        m.put(159, "Lebanon");
        m.put(160, "Lesotho");
        m.put(161, "Liberia");
        m.put(162, "Libya");
        m.put(163, "Macao");
        m.put(164, "Macedonia");
        m.put(165, "Madagascar");
        m.put(166, "Malawi");
        m.put(167, "Mali");
        m.put(168, "Marshall Islands");
        m.put(169, "Martinique");
        m.put(170, "Mauritania");
        m.put(171, "Mauritius");
        m.put(172, "Mayotte");
        m.put(173, "Micronesia");
        m.put(174, "Moldova");
        m.put(175, "Mongolia");
        m.put(176, "Montenegro");
        m.put(177, "Montserrat");
        m.put(178, "Mozambique");
        m.put(179, "Myanmar");
        m.put(180, "Namibia");
        m.put(181, "Nauru");
        m.put(182, "Nepal");
        m.put(183, "Netherlands Antilles");
        m.put(184, "New Caledonia");
        m.put(185, "Nicaragua");
        m.put(186, "Niger");
        m.put(187, "Nigeria");
        m.put(188, "Niue");
        m.put(189, "Norfolk Island");
        m.put(190, "Northern Mariana Islands");
        m.put(191, "Oman");
        m.put(192, "Pakistan");
        m.put(193, "Palau");
        m.put(195, "Palestinian territories");
        m.put(196, "Panama");
        m.put(197, "Papua New Guinea");
        m.put(198, "Paraguay");
        m.put(199, "Peru");
        m.put(200, "Philippines");
        m.put(201, "Pitcairn Islands");
        m.put(202, "Puerto Rico");
        m.put(203, "Qatar");
        m.put(204, "Reunion");
        m.put(205, "Rwanda");
        m.put(206, "Saint Helena");
        m.put(207, "Saint Kitts and Nevis");
        m.put(208, "Saint Lucia");
        m.put(209, "Saint Pierre and Miquelon");
        m.put(210, "Saint Vincent and the Grenadines");
        m.put(211, "Samoa");
        m.put(212, "San Marino");
        m.put(213, "Sao Tome and Principe");
        m.put(214, "Saudi Arabia");
        m.put(215, "Senegal");
        m.put(216, "Serbia");
        m.put(217, "Seychelles");
        m.put(218, "Sierra Leone");
        m.put(219, "Solomon Islands");
        m.put(220, "Somalia");
        m.put(221, "South Africa");
        m.put(222, "South Georgia and the South Sandwich Islands");
        m.put(223, "Sri Lanka");
        m.put(224, "Sudan");
        m.put(225, "Suriname");
        m.put(226, "Spitsbergen and Jan Mayen");
        m.put(227, "Swaziland");
        m.put(228, "Syria");
        m.put(229, "Tajikistan");
        m.put(230, "Tanzania");
        m.put(231, "Timor-Leste");
        m.put(232, "Togo");
        m.put(233, "Tokelau");
        m.put(234, "Tonga");
        m.put(235, "Trinidad and Tobago");
        m.put(236, "Tunisia");
        m.put(237, "Turkmenistan");
        m.put(238, "Turks and Caicos Islands");
        m.put(239, "Tuvalu");
        m.put(240, "Uganda");
        m.put(241, "Ukraine");
        m.put(242, "United States Minor Outlying Islands");
        m.put(243, "Uruguay");
        m.put(244, "Vanuatu");
        m.put(245, "Venezuela");
        m.put(246, "Vietnam");
        m.put(247, "British Virgin Islands");
        m.put(248, "United States Virgin Islands");
        m.put(249, "Wallis and Futuna");
        m.put(250, "Western Sahara");
        m.put(252, "Zambia");
        m.put(253, "Zimbabwe");
        m.put(254, "United Arab Emirates");
        m.put(255, "Helgoland");
        m.put(256, "Buesingen");
        m.put(258, "Curaçao");
        m.put(259, "Sint Maarten");
        m.put(260, "BES Islands");
        m.put(261, "Saint Barthélemy");
        m.put(262, "Livigno");
        m.put(263, "Campione d’Italia");
        m.put(264, "Lake Lugano from Ponte Tresa to Porto Ceresio");
        m.put(265, "Northern Ireland");
        m.put(0,   "Unknown"); 
        PLE_COUNTRY_MAP = Collections.unmodifiableMap(m);
    }


    public PlentyApiClient(String apiUrl, String username, String password) {
        this.apiUrl = apiUrl;
        this.username = username;
        this.password = password;
        this.httpClient = HttpClients.createDefault();
    }

    private void login() throws IOException {
        String url = apiUrl + "/rest/login";
        HttpPost post = new HttpPost(url);
        post.setHeader(HttpHeaders.ACCEPT, "application/json");
        post.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        String body = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password);
        post.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
        try (CloseableHttpResponse resp = httpClient.execute(post)) {
            String json = EntityUtils.toString(resp.getEntity(), StandardCharsets.UTF_8);
            if (resp.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Login-Fehler: " + json);
            }
            JsonNode tree = mapper.readTree(json);
            accessToken = tree.get("access_token").asText();
        }
    }

    public OrderData loadOrder(String orderId) throws Exception {
        if (accessToken == null) login();
        HttpGet get = new HttpGet(apiUrl + "/rest/orders/" + orderId);
        get.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        get.setHeader(HttpHeaders.ACCEPT, "application/json");
        String json = EntityUtils.toString(httpClient.execute(get).getEntity(), StandardCharsets.UTF_8);
        JsonNode root = mapper.readTree(json);
        JsonNode node = root.isArray() && root.size() > 0 ? root.get(0) : root;
        OrderData order = mapper.treeToValue(node, OrderData.class);
        for (JsonNode p : node.path("properties")) {
            if (p.path("typeId").asInt() == 7) {
                order.setInvoiceNumber(p.path("value").asText());
                break;
            }
        }
        int addrId = -1;
        for (JsonNode ar : node.path("addressRelations")) {
            if (ar.path("typeId").asInt() == 2) {
                addrId = ar.path("addressId").asInt(-1);
                break;
            }
        }
        if (addrId > 0) {
            AddressData addr = loadAddress(addrId);
            order.setRecipientName(addr.getFirstName() + " " + addr.getLastName());
            String street = addr.getStreet1() + (addr.getStreet2() != null ? " " + addr.getStreet2() : "");
            order.setStreet(street);
            order.setPostalCode(addr.getPostalCode());
            order.setCity(addr.getCity());
            order.setCountryName(addr.getCountryName());
        }
        return order;
    }

    public AddressData loadAddress(int addressId) throws Exception {
        if (accessToken == null) login();
        HttpGet get = new HttpGet(apiUrl + "/rest/accounts/addresses/" + addressId);
        get.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        get.setHeader(HttpHeaders.ACCEPT, "application/json");
        String json = EntityUtils.toString(httpClient.execute(get).getEntity(), StandardCharsets.UTF_8);
        AddressData addr = mapper.readValue(json, AddressData.class);
     // **Hier** das Plenty-Mapping: numeric countryId → lesbarer Name
        String name = PLE_COUNTRY_MAP.get(addr.getCountryId());
        addr.setCountryName(name != null ? name : "");

        return addr;
    }
}
