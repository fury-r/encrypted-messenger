package controller

import (
	"context"
	"log"

	"example.com/messenger/firebase"
	"example.com/messenger/pb"
	"example.com/messenger/utils"
	"google.golang.org/api/iterator"
)

func ContactService(ctx context.Context, req *pb.ContactsList) (*pb.ContactsList, error) {
	log.Default().Println("Validating  contacts")
	app, err := firebase.InitFirebase().Firestore(ctx)
	if err != nil {
		return nil, err
	}
	_, err = utils.GetTokenFromMetaDataAndValidate(ctx)
	if err != nil {
		return nil, err
	}
	phonesList := []string{}
	for _, x := range req.GetContacts() {
		phonesList = append(phonesList, x.PhoneNumber)

	}

	// TODO:  2023/07/16 18:06:19 Failed to iterate over query results: rpc error: code = InvalidArgument desc = 'IN' supports up to 30 comparison values. exit status 1
	iterations := len(phonesList) / 30
	batchSize := 30
	start := 0
	if len(phonesList)%batchSize != 0 {
		iterations++
	}
	contactList := []*pb.Contact{}
	log.Default().Println(batchSize, iterations)
	for i := 0; i < iterations; i++ {

		docs := app.Collection("user").Where("phoneNumber", "in", phonesList[start:start+batchSize]).Documents(ctx)
		for {
			doc, err := docs.Next()

			if err == iterator.Done {
				break
			}
			if err != nil {
				log.Fatalf("Failed to iterate over query results: %v", err)
			}
			if doc == nil {
				log.Default().Println("Document is nil")
			} else {
				verified := true
				data := doc.Data()
				log.Default().Println("looping over iter " + data["phoneNumber"].(string))
				pubKey, ok := data["pubKey"]
				key := ""
				if ok == true {
					key = pubKey.(string)
				}
				value := pb.Contact{
					Id:          " ",
					Name:        data["username"].(string),
					PhoneNumber: data["phoneNumber"].(string),
					PubKey:      &key,
					Uuid:        &doc.Ref.ID,
					IsVerified:  &verified,
				}
				contactList = append(contactList, &value)
			}

		}
		start += batchSize
	}

	log.Default().Println("ContactList ", len(contactList))
	return &pb.ContactsList{
		Contacts: contactList,
	}, nil

}
