const mockRotateKey = jest.fn();
const mockUpdateUpiIntegration = jest.fn();
const mockUpdateEcommerceIntegration = jest.fn();
const mockGetClient = jest.fn();

jest.mock("../../src/common/GrpcClientService", () => ({
  GrpcClientService: jest.fn().mockImplementation(() => ({
    getClient: mockGetClient,
  })),
}));

import { rotateKey } from "../../src/services/keyRotation";
import { updateUpiIntegration } from "../../src/services/upi";
import { updateEcommerceIntegration } from "../../src/services/ecommerce";

describe("integration services", () => {
  beforeEach(() => {
    jest.clearAllMocks();
    mockGetClient.mockReturnValue({
      rotateKey: mockRotateKey,
      updateUpiIntegration: mockUpdateUpiIntegration,
      updateEcommerceIntegration: mockUpdateEcommerceIntegration,
    });
    mockRotateKey.mockImplementation((_request, _metadata, callback) => {
      callback(null, { rotated: true });
      return true;
    });
    mockUpdateUpiIntegration.mockImplementation(
      (_request, _metadata, callback) => {
        callback(null, { enabled: true });
        return true;
      }
    );
    mockUpdateEcommerceIntegration.mockImplementation(
      (_request, _metadata, callback) => {
        callback(null, { enabled: true });
        return true;
      }
    );
  });

  it("forwards rotateKey requests with authorization metadata", async () => {
    const callback = jest.fn();
    const request = {
      metadata: {
        get: jest.fn().mockReturnValue(["Bearer token"]),
      },
      request: {
        pubKey: "rotated-public-key",
      },
    };

    await rotateKey(request, callback);

    expect(mockRotateKey).toHaveBeenCalledTimes(1);
    expect(mockRotateKey.mock.calls[0][0]).toEqual(request.request);
    expect(mockRotateKey.mock.calls[0][1].get("authorization")).toEqual([
      "Bearer token",
    ]);
    expect(callback).toHaveBeenCalledWith("", { rotated: true });
  });

  it("forwards updateUpiIntegration requests with authorization metadata", async () => {
    const callback = jest.fn();
    const request = {
      metadata: {
        get: jest.fn().mockReturnValue(["Bearer token"]),
      },
      request: {
        upiId: "alice@upi",
        provider: "test-provider",
        enabled: true,
      },
    };

    await updateUpiIntegration(request, callback);

    expect(mockUpdateUpiIntegration).toHaveBeenCalledTimes(1);
    expect(mockUpdateUpiIntegration.mock.calls[0][0]).toEqual(request.request);
    expect(
      mockUpdateUpiIntegration.mock.calls[0][1].get("authorization")
    ).toEqual(["Bearer token"]);
    expect(callback).toHaveBeenCalledWith("", { enabled: true });
  });

  it("forwards updateEcommerceIntegration requests with authorization metadata", async () => {
    const callback = jest.fn();
    const request = {
      metadata: {
        get: jest.fn().mockReturnValue(["Bearer token"]),
      },
      request: {
        merchantId: "merchant-123",
        catalogUrl: "https://catalog.example.com",
        enabled: true,
      },
    };

    await updateEcommerceIntegration(request, callback);

    expect(mockUpdateEcommerceIntegration).toHaveBeenCalledTimes(1);
    expect(mockUpdateEcommerceIntegration.mock.calls[0][0]).toEqual(
      request.request
    );
    expect(
      mockUpdateEcommerceIntegration.mock.calls[0][1].get("authorization")
    ).toEqual(["Bearer token"]);
    expect(callback).toHaveBeenCalledWith("", { enabled: true });
  });
});
