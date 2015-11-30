###Offer Key API Document
Please refer [Integration Document](https://drive.google.com/open?id=0B4URmsDLhGXmOWczQ3A5dnBOUzNEV2lwSVloaDFybnNUQUR3) goto page 40 for offer key API.

#####Description of Offer key hash 
Please refer [Server Side Document](https://github.com/payu-intrepos/Documentations/wiki/4.-Server-Side) for hash generation.
To call any PayU API, you need hash:
 
 ```sh
sha512(key|command|var1|salt)
```
```sh
  key=YOUR KEY
  command=check_offer_status
  salt= YOUR SALT
  var1=YOUR OFFER KEY
```
Example

```sh
key=gtKFFx(YOUR KEY)
hash=e43ede14c88c70e27f47001b4207a......(generated using sha512(key|command|var1|salt) from your server)
command=check_offer_status
var1=guru_offer@7260(YOUR OFFER KEY)
```

You need to pass generated hash using above logic to post data which is being send to PayU server.

#####PayU response 
When API is hit from app, PayU will give response back in json format : 
```sh
{"status":1,"msg":"Valid offer",
"discount":5,
"category":"creditcard",
"offer_key":"guru_offer@7260",
"offer_type":"instant",
"offer_availed_count":"Unknown",
"offer_remaining_count":"Unknown"}

```
 
