# 1.MIME headers
[Concept of MEME headers](https://learn.microsoft.com/en-us/previous-versions/office/developer/exchange-server-2010/aa563068(v=exchg.140))

# 2. What are the different types of proxy?How to use HTTP proxy?
##  Different Types of Proxies
Proxies act as intermediaries between your device and the internet. They can be categorized by how they handle data, their anonymity level, or their network architecture.
## By Functionality & Protocol

* HTTP Proxy: Designed specifically for web traffic (HTTP/HTTPS). It intercepts web requests from your browser, hides your IP address, and fetches the webpage for you.
* SOCKS Proxy (SOCKS4/SOCKS5): A more versatile proxy that works at a lower network level. It doesn't read the web traffic, meaning it can handle any type of data or protocol, including email (SMTP), file sharing (P2P/Torrenting), and gaming.
* Transparent Proxy: Often used by companies or schools to filter content or cache data. It does not hide your IP address or modify requests; you often don't even know it is there.

## By Anonymity Level

* Anonymous Proxy: Hides your real IP address but tells the destination server that you are using a proxy.
* Elite / High-Anonymity Proxy: Hides your IP address and completely conceals the fact that you are using a proxy. The web server thinks you are a regular residential user.

## By IP Source

* Residential Proxy: Uses IP addresses assigned by Internet Service Providers (ISPs) to real homes. They look like genuine users, making them highly reliable and less likely to be blocked.
* Datacenter Proxy: Uses IPs hosted in massive server warehouses. They are fast and cheap but easily detected and blocked by websites looking for automated traffic.

------------------------------

# 3. How to Use an HTTP Proxy

To use an HTTP proxy, you need three pieces of information from your proxy provider: the IP address (or Hostname), the Port number, and Authentication credentials (Username & Password, if required).
## Method A: Setting it up in a Web Browser (e.g., Google Chrome / Edge)

Browsers typically inherit their settings from your operating system.

   1. Open your browser settings and search for "Proxy".
   2. Click Open your computer's proxy settings.
   3. Under Manual Proxy Setup, toggle Use a proxy server to On.
   4. Enter the Address and Port, then click Save.
   5. When you open a webpage, a box will pop up asking for your proxy Username and Password (if applicable).

## Method B: Using it in Java Code (Connecting to your previous example)
If you want your Java network code (like HttpURLConnection) to route through a proxy, you pass a Proxy object when opening the connection:

[Click Here to To get code](/Unit3_Codes/ProxyExample.java)


## 4. How can i access to google.com but deny facebook and youtube by the using proxy server in middle.Help me understand the process and also provide the code that teaches me to do so.

To control website access using a proxy server in the middle, you need to implement a Filtering Reverse/Forward Proxy.

When your browser or device makes a web request, the proxy acts as a gatekeeper. It reads the target destination (the URL or Hostname) from the incoming request headers, matches it against a whitelist or blacklist, and decides whether to forward the request to the internet or block it.
------------------------------
## The Process: Step-by-Step

   1. Interception: Your browser sends an HTTP request targeting a website (e.g., facebook.com) directly to your proxy server instead of the public internet.
   2. Parsing: The proxy server parses the incoming network stream and extracts the target host destination from the MIME/HTTP request headers (specifically looking at the Host header or the full request URI).
   3. Evaluation: The proxy evaluates the hostname against custom rules:
   * Is it google.com? Allowed. Proceed.
      * Is it facebook.com or youtube.com? Denied. Trigger block action.
   4. Action (Allowed): If allowed, the proxy opens a client socket to the real destination (google.com), fetches the data, and streams it back to your device.
   5. Action (Denied): If denied, the proxy terminates the request immediately and sends a custom HTTP status code back to your browser (like 403 Forbidden) along with an error page.

------------------------------
## Educational Java Code Implementation
[Click Here to view Code ](/Unit3_Codes/FilteringProxyServer.java)

## How to Test this Setup

   1. Run the Code: Execute the Java file on your machine. It will sit waiting for traffic on port 8888.
   2. Configure Your Browser: Go to your browser's network settings and point your proxy configuration manually to IP 127.0.0.1 and Port 8888.
   3. Test Allowed Site: Navigate to an unencrypted version of your target or run a simple local web client routing through it to check google.com. The code logs [ACCESS ALLOWED].
   4. Test Blocked Site: Try to load http://facebook.com. The proxy instantly hijacks the stream, logs [ACCESS DENIED], and returns a clean, custom HTML web warning page reading 403 Access Denied instead of connecting to Facebook.



# 5. Differnt Sockets used in Lab 10
Welcome to networking! It is completely normal to feel confused by sockets at first.

Think of a Socket like a physical plug or an open telephone line that allows two programs to talk to each other over a network. Without sockets, data would just sit on your hard drive with no way to travel out into the world.
In our proxy server code, we used three distinct types of sockets because the proxy has three different jobs to do. Let's break them down using a real-world analogy.

------------------------------
## The Analogy: A Hotel Concierge Desk
Imagine you are at a hotel. You want to order food, but the hotel rules say you aren't allowed to call restaurants directly. You must use the Hotel Concierge (the Proxy Server).
Here is how the sockets in our code match this scenario:

| Socket Name in Code | What it represents in the Hotel Analogy | What it actually does in the Code |
|---|---|---|
| ServerSocket | The main Concierge Desk in the lobby. | It stays open and sits on a specific port (8888), waiting for a browser to knock and ask for help. |
| clientSocket | The private telephone line running directly from your hotel room to the concierge desk. | This is the dedicated connection between your web browser and the proxy server. |
| targetSocket | The second phone line the concierge uses to call the outside restaurant (like Google). | This is the connection the proxy server opens to the real website on the internet. |

------------------------------
## Why each socket is necessary (Step-by-Step)## 1. The ServerSocket (The Listener)

ServerSocket serverSocket = new ServerSocket(PORT);
Socket clientSocket = serverSocket.accept();


* Why it's used: Your computer runs dozens of programs at once. The ServerSocket claims a specific port number (like 8888) so the operating system knows that any web traffic sent to port 8888 belongs strictly to your proxy program.
* What it does: It calls a method named .accept(). This freezes the code and makes the server sit quietly and listen. The moment your browser tries to load a page, the ServerSocket wakes up and accepts the connection.

## 2. The clientSocket (The Browser Connection)

private static void handleClient(Socket clientSocket) { ... }


* Why it's used: Once the ServerSocket accepts the browser's knock on the door, it immediately hands off the conversation to a new, dedicated clientSocket and goes right back to listening for the next person.
* What it does: The proxy uses this socket's InputStream to read what website the browser wants to visit (e.g., “Hey proxy, please get me google.com”). Later, if a site is blocked, the proxy uses this same socket's OutputStream to shout back: “Access Denied!”

## 3. The targetSocket (The Internet Connection)

Socket targetSocket = new Socket(host, 80);


* Why it's used: If the proxy checks the request and decides the website is allowed (like google.com), the proxy cannot just magically make the browser see it. The proxy itself must go out to the internet, act like a browser, and fetch the webpage.
* What it does: It creates a targetSocket pointed at the real website's server (on port 80, the standard web port). It acts as a middleman bridge: it takes the webpage data coming in from the targetSocket and shoves it right back out through the clientSocket to your browser.

------------------------------
## Summary of the Data Flow

   1. Data leaves your browser → enters proxy via clientSocket.
   2. Proxy reads data, approves it → leaves proxy via targetSocket to the internet.
   3. Google responds → enters proxy via targetSocket.
   4. Proxy passes it along → leaves proxy via clientSocket back to your browser.



