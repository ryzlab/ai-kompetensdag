# Chain of draft

A new prompting technique that boils down chain of thought to only the
essential elements

Att undersöka
 * Standard
 * Chain of thought (CoT)
 * Chain of draft (CoD)
https://www.youtube.com/watch?v=rYnisU10wu0

Standard
Answer the question directly. Do not return any preamble, explanation or reasoning

Chain of thought
Think step by step to answer the following question.
Return the answer at the end of the response after a separator #####.

Chain of draft
Think step by step, but only keep an minimum draft for each thinking step,
with 5 words at most. Return the answer at the end of the response after a
separator #####.

Q: Jason had 20 lollipops. He gave Danny some lollipops. Now Jason
has 12 lollipops. How many lollipops did Jason give to Danny?

GSM8K evaluation results
Model     Prompt    Accuracy  Token # Latency
          Standard  53.3%     1.1     0.6s
GPT-4o    CoT       95.4%     205.1   4.2s
          CoD       91.1%     43.9    1.0s

No new technology, just change the prompt
