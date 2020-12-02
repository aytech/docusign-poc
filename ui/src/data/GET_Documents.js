module.exports = [
	{
		"pid": "Oleg_Test-30-2-LATEST",
		"author": "Oleg Yapparov",
		"fileName": "prague1.jpg",
		"acl": "Public",
		"resource": {
			"name": null,
			"size": null,
			"mime": "image/png",
			"fileName": "image/png",
			"url": "https://idm-ade-8.idm.awsdev.infor.com/ca/api/resources/Oleg_Test-30-2-LATEST/SmallPreview?$token=AapeUT8LO%2FKWqfepUSwVMRRti1t3xCDgSkjh2u5845X8E6IpKzRClaL7B41HjNdbC6N%2FNwjyKgh46YKwwfFJsX%2FrDlt6ToqnnP3ekZ5QgxjCdrEezO4mc6E8ouz5ALQqxVDzo5%2BD5%2FN0mtOtt9e6MEMhaXbEF22wd3gqY%2FM0%2BxZNW7y6b4aQBUYwffL3ZWXu3ywzBq5N8642O9qIPNJx1DOEeUL5q%2BD7aTRghAQUKrT47SL%2BlwQxkeOy7nFBodozr3qPebpnoj3tUM%2BUbTH9%2FpeXh4KoVp6PsA%3D%3D&$tenant=CI_TST"
		},
		"attributes": [
			{
				"name": "Name",
				"type": "STRING",
				"value": "FPLM attire"
			}
		],
		"envelopes": []
	},
	{
		"pid": "Oleg_Test-31-3-LATEST",
		"author": "Oleg Yapparov",
		"fileName": "prague2.jpg",
		"acl": "Public",
		"resource": {
			"name": null,
			"size": null,
			"mime": "image/png",
			"fileName": "image/png",
			"url": "https://idm-ade-8.idm.awsdev.infor.com/ca/api/resources/Oleg_Test-31-3-LATEST/SmallPreview?$token=Abi79hu94jpoEBIY4OrGZw4VOLd689Sdt2EwSF15B4R8Fwv2XA8ijItUXurGjNvJ2TQM7v20CFf1FZRqUsfQTiKj79hlm5Ff3BmNZqqaiaphhbk0e9%2F0fAOf1AL6cs6XRjWJNdyirWpwHPerFdHgjxEPFriGvxrIEpT8Aku3AAKcH0eq%2BeihTeYE2LdDXLGPuAuVrB%2FjCwIY22KY5%2BNr9k9aEWixcmEM5MZGwQEzAUbgORiJcx3Zz9HoiHiMUCOG8ieR1iwU6%2FWZrisVaf%2B3akd81Zv3d6RzUg%3D%3D&$tenant=CI_TST"
		},
		"attributes": [
			{
				"name": "Name",
				"type": "STRING",
				"value": "FPLM garment"
			}
		],
		"envelopes": [
			{
				"signature_id": "9ee41a34-ad9a-4b64-8d0e-e1f61ce0e817",
				"status": "in progress",
				"subject": "Sample signature request - POC",
				"message": "DocuSign service for Infor.",
				"sender": null,
				"recipients": [
					{
						"name": "Oleg Yapparov",
						"email": "Oleg.Yapparov@infor.com",
						"status": "in progress",
						"order": 0,
						"sent": 1606841039730,
						"declined": 0,
						"completed": 0
					}
				],
				"documents": [
					{
						"pid": "Oleg_Test-30-2-LATEST",
						"name": "prague1.jpg",
						"file": "prague1.jpg"
					},
					{
						"pid": "Oleg_Test-31-3-LATEST",
						"name": "prague2.jpg",
						"file": "prague2.jpg"
					}
				]
			}
		]
	}
]