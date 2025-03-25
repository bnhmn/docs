import { describe, expect, it } from "vitest";

import { sprClient } from "../../src/cis-client";
import { createSepaQrCode } from "../../src/generated/cis-client";
import { createFullSepaQrCodeRequest, createMinimalSepaQrCodeRequest } from "./model/request/qr-codes";

describe("Create a SEPA QR Code", () => {
  it("1 | should create a QR code (minimal)", async () => {
    const { data, response } = await createSepaQrCode({
      client: sprClient,
      body: createMinimalSepaQrCodeRequest,
    });

    const decodedData = await decodeQrCode(data);
    const decodedLines = decodedData.split("\n");

    expect(response.status).toBe(200);
    expect(decodedLines.length).toBeGreaterThan(1);
  });

  it("2 | should create a QR code (full)", async () => {
    const { data, response } = await createSepaQrCode({
      client: sprClient,
      body: createFullSepaQrCodeRequest,
    });

    const decodedData = await decodeQrCode(data);
    const decodedLines = decodedData.split("\n");

    expect(response.status).toBe(200);
    expect(decodedLines.length).toBeGreaterThan(1);
  });
});

async function decodeQrCode(data: any) {
  const bufferArray = await data.arrayBuffer();
  const image = await Jimp.read(Buffer.from(bufferArray));

  const imageData = {
    data: new Uint8ClampedArray(image.bitmap.data),
    width: image.bitmap.width,
    height: image.bitmap.height,
  };

  const decodedQR = jsQR(imageData.data, imageData.width, imageData.height);
  return decodedQR!.data;
}
