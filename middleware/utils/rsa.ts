import * as crypto from "crypto";

export function encryptMessage(message: string, publicKey: any) {
  const buffer = Buffer.from(message, "utf-8");
  const publicKeyBuffer = Buffer.from(publicKey, "utf-8");
  const encryptedBuffer = crypto.publicEncrypt(
    {
      key: publicKeyBuffer,
      padding: crypto.constants.RSA_PKCS1_PADDING,
    },
    buffer
  );

  return encryptedBuffer.toString("base64");
}
