const mockVerifyToken = jest.fn();
const mockGetClient = jest.fn();
const mockSendToQueue = jest.fn();

jest.mock("../../src/common/GrpcClientService", () => ({
  GrpcClientService: jest.fn().mockImplementation(() => ({
    getClient: mockGetClient,
  })),
}));

jest.mock("../../src/common/rabbitMQ", () => ({
  RabbitMQ: jest.fn().mockImplementation(() => ({
    sendToQueue: mockSendToQueue,
  })),
}));

import { handShakeRequest } from "../../src/services/handShake";

describe("handShakeRequest", () => {
  beforeEach(() => {
    jest.clearAllMocks();
    mockGetClient.mockReturnValue({
      verifyToken: mockVerifyToken,
    });
    mockVerifyToken.mockImplementation((_request, _metadata, callback) => {
      callback(null, {});
      return true;
    });
  });

  it("queues the exchange for the receiver after adding the authorization metadata", async () => {
    const callback = jest.fn();
    const request = {
      metadata: {
        get: jest.fn().mockReturnValue(["Bearer token"]),
      },
      request: {
        exchange: "encrypted-key",
        reciever: "user-2",
      },
    };

    await handShakeRequest(request, callback);

    expect(mockVerifyToken).toHaveBeenCalledTimes(1);
    expect(mockVerifyToken.mock.calls[0][1].get("authorization")).toEqual([
      "Bearer token",
    ]);
    expect(mockSendToQueue).toHaveBeenCalledWith(
      JSON.stringify("encrypted-key"),
      "user-2"
    );
    expect(callback).not.toHaveBeenCalled();
  });

  it("does not queue the exchange when the receiver is missing", async () => {
    const callback = jest.fn();
    const request = {
      metadata: {
        get: jest.fn().mockReturnValue(["Bearer token"]),
      },
      request: {
        exchange: "encrypted-key",
      },
    };

    await handShakeRequest(request, callback);

    expect(mockSendToQueue).not.toHaveBeenCalled();
    expect(callback).not.toHaveBeenCalled();
  });
});
