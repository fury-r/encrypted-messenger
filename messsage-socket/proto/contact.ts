// Original file: ../protobuf/service/contact.proto


export interface Contact {
  'id'?: (string);
  'name'?: (string);
  'phoneNumber'?: (string);
  'countryCode'?: (string);
  'profilePicture'?: (string);
  'isVerified'?: (boolean);
  'uuid'?: (string);
  'pubKey'?: (string);
  '_countryCode'?: "countryCode";
  '_profilePicture'?: "profilePicture";
  '_isVerified'?: "isVerified";
  '_uuid'?: "uuid";
  '_pubKey'?: "pubKey";
}

export interface Contact__Output {
  'id'?: (string);
  'name'?: (string);
  'phoneNumber'?: (string);
  'countryCode'?: (string);
  'profilePicture'?: (string);
  'isVerified'?: (boolean);
  'uuid'?: (string);
  'pubKey'?: (string);
}
