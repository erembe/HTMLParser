package pl.birek.util.web;

public enum WebProtocol {
    ARP, DHCP, DNS, FTP, SFTP, HTTP, HTTPS, IMAP, ICMP, IDRP, IP, IRC, POP3, PAR, RLOGIN, SMTP, SSL, SSH, TCP, TELNET, UPD, UPS, UNKNOWN;

    public static boolean compareToString(WebProtocol protocol, String str){
        switch (protocol){
            case ARP: return str.toUpperCase().equals("ARP");
            case DHCP: return str.toUpperCase().equals("DHCP");
            case DNS: return str.toUpperCase().equals("DNS");
            case FTP: return str.toUpperCase().equals("FTP");
            case SFTP: return str.toUpperCase().equals("SFTP");
            case HTTP: return str.toUpperCase().equals("HTTP");
            case HTTPS: return str.toUpperCase().equals("HTTPS");
            case IMAP: return str.toUpperCase().equals("IMAP");
            case ICMP: return str.toUpperCase().equals("ICMP");
            case IDRP: return str.toUpperCase().equals("IDRP");
            case IP: return str.toUpperCase().equals("IP");
            case IRC: return str.toUpperCase().equals("IRC");
            case POP3: return str.toUpperCase().equals("POP3");
            case PAR: return str.toUpperCase().equals("PAR");
            case RLOGIN: return str.toUpperCase().equals("RLOGIN");
            case SMTP: return str.toUpperCase().equals("SMTP");
            case SSL: return str.toUpperCase().equals("SSL");
            case SSH: return str.toUpperCase().equals("SSH");
            case TCP: return str.toUpperCase().equals("TCP");
            case TELNET: return str.toUpperCase().equals("TELNET");
            case UPD: return str.toUpperCase().equals("UPD");
            case UPS: return str.toUpperCase().equals("UPS");
        }
        return false;
    }
}