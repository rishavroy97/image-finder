"use server";

import { z } from "zod";

const FETCH_URL = "http://localhost:8080/graphql";

const FormSchema = z.object({
  url: z.coerce.string().url({ message: "Please enter a valid url." }),
  name: z.coerce.string(),
  depth: z.coerce
    .number()
    .gt(0, { message: "Please enter a depth greater than 0." }),
});

export type State = {
  errors?: {
    url?: string[];
    name?: string[];
    depth?: string[];
  };
  message?: string | null;
  data?: any;
};

export const searchImages = async (_prevState: State, formData: FormData) => {
  // reset form
  if (formData.entries().next().done) {
    return {};
  }

  const validatedFields = FormSchema.safeParse({
    url: formData.get("url"),
    name: formData.get("name"),
    depth: formData.get("depth"),
  });

  // If form validation fails, return errors early. Otherwise, continue.
  if (!validatedFields.success) {
    return {
      errors: validatedFields.error.flatten().fieldErrors,
      message: "Incorrect/Missing fields. Failed to fetch images.",
    };
  }

  // Prepare data for insertion into the database
  const { url, name, depth } = validatedFields.data;

  try {
    const requestBody = {
      query: `
        query 
            findImages { findImages(website: { name: "${name}", url: "${url}", levels: ${depth} }) {
                id
                url
                name
                images {
                    id
                    url
                }
            }
        }`,
    };

    const headers = {
      "Content-Type": "application/json",
    };

    const options = {
      method: "POST",
      headers,
      body: JSON.stringify(requestBody),
    };

    const response = await fetch(FETCH_URL, options);

    if (!response.ok) {
      throw new Error("Failed to submit data. Please try again.");
    }

    const data = await response.json();

    if (data && data.errors) {
      return { message: "Server Error: Failed to fetch images." };
    }

    return { data: data?.data?.findImages };
  } catch (error) {
    return { message: "Server Error: Failed to fetch images." };
  }
};
