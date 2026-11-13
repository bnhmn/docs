# Local Text-to-Speech

Local text-to-speech using [mlx-audio](https://github.com/Neywa-Labs/mlx-audio) and
[Qwen3-TTS](https://github.com/Neywa-Labs/mlx-audio/blob/main/mlx_audio/tts/models/qwen3_tts/README.md).

## Getting Started

1. Make sure that [Python](https://www.python.org/) is installed on your system.

2. Install mlx-audio

   ```bash
   python3 -m venv venv
   source venv/bin/activate
   pip install "mlx-audio[server]"
   ```

3. Generate a voice sample to use as a reference:

   ```bash
   for i in {1..6}; do
     mlx_audio.tts.generate \
       --model mlx-community/Qwen3-TTS-12Hz-1.7B-VoiceDesign-bf16 \
       --lang_code german \
       --instruct "Ein deutscher Muttersprachler, männlich, mittlere Tonlage, ruhige und freundliche Stimme, neutrales und akzentfreies Hochdeutsch." \
       --speed 1.15 \
       --temperature 0.55 \
       --top_p 0.8 \
       --repetition_penalty 1.15 \
       --text "Bei der Entwicklung moderner Sprachsysteme achten wir besonders darauf, dass alle Sätze flüssig, ruhig und ohne störende Nebengeräusche übertragen werden." \
       --file_prefix "voice/sample$i"
   done
   ```

4. Convert text to speech

   ```bash
   mlx_audio.tts.generate \
     --model mlx-community/Qwen3-TTS-12Hz-1.7B-Base-bf16 \
     --lang_code german \
     --ref_audio voice/sample5_000.wav \
     --ref_text "Bei der Entwicklung moderner Sprachsysteme achten wir besonders darauf, dass alle Sätze flüssig, ruhig und ohne störende Nebengeräusche übertragen werden." \
     --temperature 0.55 \
     --top_p 0.8 \
     --repetition_penalty 1.15 \
     --text "Heute prüfen wir die akustische Qualität unserer neuen Sprachausgabe, damit alle Wörter sauber, ruhig und vollkommen verständlich klingen." \
     --file_prefix output/audio
   ```

## Web Server

1. Start the local OpenAI-compatible server

   ```bash
   mlx_audio.server --port 8000
   ```

2. Install the Node.js OpenAI SDK

   ```bash
   npm install openai
   ```

3. Call the API from Node.js

   ```js
   // text-to-speech.js
   import fs from "fs";
   import OpenAI from "openai";

   const openai = new OpenAI({
     // Local MLX server
     baseURL: "http://localhost:8000/v1",
     apiKey: "not-needed",
   });

   async function main({ outputFileName = "output/audio" } = {}) {
     const response = await openai.audio.speech.create({
       model: "mlx-community/Qwen3-TTS-12Hz-1.7B-Base-bf16",
       voice: "default", // Required by OpenAI schema, ignored when using ref_audio
       response_format: "wav",
       input:
         "Heute prüfen wir die akustische Qualität unserer neuen Sprachausgabe, damit alle Wörter sauber, ruhig und vollkommen verständlich klingen.",
       // Custom mlx-audio parameters passed alongside standard OpenAI fields
       lang_code: "german",
       ref_audio: "voice/sample5_000.wav",
       ref_text:
         "Bei der Entwicklung moderner Sprachsysteme achten wir besonders darauf, dass alle Sätze flüssig, ruhig und ohne störende Nebengeräusche übertragen werden.",
       temperature: 0.55,
       top_p: 0.8,
       repetition_penalty: 1.15,
     });
     const fileExt = response.headers
       .get("content-disposition")
       .match(/filename="?[^";]*\.([^".;]+)"?/i)?.[1];

     const buffer = Buffer.from(await response.arrayBuffer());
     await fs.promises.writeFile(`${outputFileName}.${fileExt}`, buffer);
     console.log(`Audio successfully saved to ${outputFileName}.${fileExt}`);
   }

   main();
   ```

4. Run the Node.js script

   ```bash
   node text-to-speech.js
   ```
